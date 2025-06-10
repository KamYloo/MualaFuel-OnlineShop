import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { format } from 'date-fns';
import {
    deleteEmail,
    fetchEmails,
    fetchEmailBody
} from '../redux/EmailService/Action.js';
import { EmailPreview } from "../features/EmailHistory/EmailPreview.jsx";
import Spinner from "../components/Spinner.jsx";
import ErrorOverlay from "../components/ErrorOverlay.jsx";

export function EmailHistory() {
    const dispatch = useDispatch();
    const { emails, pageInfo, loading, error, previewBody, previewLoading, previewError } = useSelector(state => state.emailServ);

    const [filters, setFilters] = useState({
        recipient: '',
        subject: '',
        from: '',
        to: '',
        relatedToOrder: false
    });

    const [appliedFilters, setAppliedFilters] = useState(filters);
    const [page, setPage] = useState(0);
    const [showPreview, setShowPreview] = useState(false);

    useEffect(() => {
        const params = {
            page,
            size: pageInfo.size,
            recipient: appliedFilters.recipient ? `%${appliedFilters.recipient}%` : undefined,
            subject: appliedFilters.subject ? `%${appliedFilters.subject}%` : undefined,
            from: appliedFilters.from || undefined,
            to: appliedFilters.to || undefined,
            relatedToOrder: appliedFilters.relatedToOrder || undefined
        };
        dispatch(fetchEmails(params));
    }, [dispatch, appliedFilters, page, pageInfo.size]);

    const handleFilterChange = e => {
        const { name, value, type, checked } = e.target;
        setFilters(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleApply = () => {
        setPage(0);
        setAppliedFilters(filters);
    };

    const handleDeleteClick = id => {
        if (window.confirm('Are you sure you want to delete this email?')) {
            dispatch(deleteEmail(id));
        }
    };

    const handlePreviewClick = id => {
        dispatch(fetchEmailBody(id));
        setShowPreview(true);
    };

    if (loading) return <Spinner size={350} />
    if (error) return <ErrorOverlay size={350} message={error}/>

    return (
        <div className="min-h-screen p-8" style={{ backgroundColor: '#f5e9dc' }}>
            <div className="max-w-6xl mx-auto bg-white rounded-lg shadow-md p-6">
                <h1 className="text-3xl font-bold text-[#3E2723] mb-4">Email History</h1>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                    {['recipient', 'subject'].map(field => (
                        <input
                            key={field}
                            name={field}
                            placeholder={field === 'recipient' ? 'Recipient' : 'Subject'}
                            value={filters[field]}
                            onChange={handleFilterChange}
                            className="border rounded p-2"
                        />
                    ))}
                    <div className="flex space-x-2">
                        <input
                            type="datetime-local"
                            name="from"
                            value={filters.from}
                            onChange={handleFilterChange}
                            className="border rounded p-2 w-1/2"
                        />
                        <input
                            type="datetime-local"
                            name="to"
                            value={filters.to}
                            onChange={handleFilterChange}
                            className="border rounded p-2 w-1/2"
                        />
                    </div>
                    <label className="flex items-center space-x-2">
                        <input
                            type="checkbox"
                            name="relatedToOrder"
                            checked={filters.relatedToOrder}
                            onChange={handleFilterChange}
                            className="border rounded"
                        />
                        <span>Related to Order</span>
                    </label>
                    <button
                        onClick={handleApply}
                        className="bg-[#3E2723] text-white px-4 py-2 rounded-lg hover:bg-[#4E3423] transition-colors col-span-full md:col-auto"
                    >
                        Apply Filters
                    </button>
                </div>

                <table className="w-full table-auto mb-4">
                    <thead>
                    <tr className="bg-[#3E2723] text-white">
                        {['ID', 'Recipient', 'Subject', 'Sent At', 'Actions'].map(text => (
                            <th key={text} className="px-4 py-2">{text}</th>
                        ))}
                    </tr>
                    </thead>
                    <tbody>
                    {emails.map(email => (
                        <tr key={email.id} className="text-center border-b">
                            <td className="px-4 py-2">{email.id}</td>
                            <td className="px-4 py-2">{email.recipient}</td>
                            <td className="px-4 py-2">{email.subject}</td>
                            <td className="px-4 py-2">
                                {format(new Date(email.sentAt), 'yyyy-MM-dd HH:mm')}
                            </td>
                            <td className="px-4 py-2">
                                <button
                                    onClick={() => handlePreviewClick(email.id)}
                                    className="bg-[#3E2723] text-white px-3 py-1 rounded-lg hover:bg-[#4E3423] transition-colors mr-2"
                                >
                                    Preview
                                </button>
                                <button
                                    onClick={() => handleDeleteClick(email.id)}
                                    className="bg-[#3E2723] text-white px-3 py-1 rounded-lg hover:bg-[#4E3423] transition-colors"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <div className="flex justify-between">
                    <button
                        onClick={() => setPage(prev => Math.max(prev - 1, 0))}
                        disabled={page === 0}
                        className="px-4 py-2 rounded border"
                    >
                        Previous
                    </button>
                    <span>Page {page + 1} of {pageInfo.totalPages}</span>
                    <button
                        onClick={() => setPage(prev => Math.min(prev + 1, pageInfo.totalPages - 1))}
                        disabled={page + 1 === pageInfo.totalPages}
                        className="px-4 py-2 rounded border"
                    >
                        Next
                    </button>
                </div>
            </div>

            <EmailPreview
                show={showPreview}
                onClose={() => setShowPreview(false)}
                loading={previewLoading}
                error={previewError}
                htmlContent={previewBody}
            />
        </div>
    );
}

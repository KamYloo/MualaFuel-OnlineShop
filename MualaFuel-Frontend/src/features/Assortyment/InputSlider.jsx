import Slider from "rc-slider";
import React from "react";

export function InputSlider({value, setValue, min, max}) {
    return (
        <div className="flex items-center">
            <input type="number"
                   className="w-14"
                   min={min}
                   max={max}
                   onChange={(e) =>
                       setValue([e.target.value, value[1]])}
                   value={value[0]}
            />
            <Slider
                className="mx-3"
                range
                min={min}
                max={max}
                allowCross={false}
                onChange={(value) => setValue(value)}
                value={value}
            />
            <input type="number"
                   className="w-14"
                   min={min}
                   max={max}
                   onChange={(e) =>
                       setValue([value[0], e.target.value])}
                   value={value[1]}
            />
        </div>
    )
}
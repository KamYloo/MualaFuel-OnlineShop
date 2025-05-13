package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.dto.ProductSearchDto;
import com.example.MualaFuel_Backend.entity.Product;
import com.example.MualaFuel_Backend.enums.AlcoholType;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

    public Product save(Product product) {
        final String SQL_INSERT = "INSERT INTO product (name, description, price, brand, alcohol_type, quantity, " +
                "alcohol_content, capacity_in_milliliters, image_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getBrand());
            statement.setString(5, product.getAlcoholType().name());
            statement.setInt(6, product.getQuantity());
            statement.setDouble(7, product.getAlcoholContent());
            statement.setInt(8, product.getCapacityInMilliliters());
            statement.setString(9, product.getImagePath());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }

            conn.commit();
            return product;

        } catch (SQLException ex) {
            throw new RuntimeException("Error saving product", ex);
        }
    }

    public Optional<Product> findById(Long id) {
        final String SQL_SELECT = "SELECT * FROM product WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL_SELECT)) {

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error finding product by id: " + id, ex);
        }
    }

    public Page<Product> findAll(Pageable pageable, ProductSearchDto productSearch) {
        String SQL_SELECT = "SELECT * FROM product";
        String countQuery = "SELECT COUNT(*) FROM product";

        SQL_SELECT += stringSearchBuilder(productSearch);
        SQL_SELECT += "LIMIT ? OFFSET ?";

        System.out.println(SQL_SELECT);

        List<Product> products = new ArrayList<>();
        long totalElements = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement dataStatement = conn.prepareStatement(SQL_SELECT)){

            dataStatement.setInt(1, pageable.getPageSize());
            dataStatement.setInt(2, (int)pageable.getOffset());

             ResultSet rs = dataStatement.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

            try(Connection connCount = ConnectionFactory.getConnection();
            PreparedStatement countStatement = connCount.prepareStatement(countQuery)) {
                ResultSet countRs = countStatement.executeQuery();
                if (countRs.next()) {
                    totalElements = countRs.getLong(1);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error retrieving all products", ex);
        }
        return new PageImpl<>(products, pageable, totalElements);
    }

    public void update(Product product) {
        final String SQL_UPDATE = "UPDATE product SET name = ?, description = ?, price = ?, brand = ?, " +
                "alcohol_type = ?, quantity = ?, alcohol_content = ?, " +
                "capacity_in_milliliters = ?, image_path = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getBrand());
            statement.setString(5, product.getAlcoholType().name());
            statement.setInt(6, product.getQuantity());
            statement.setDouble(7, product.getAlcoholContent());
            statement.setInt(8, product.getCapacityInMilliliters());
            statement.setString(9, product.getImagePath());
            statement.setLong(10, product.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Updating product failed, no rows affected.");
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating product", ex);
        }
    }

    public void delete(Long id) {
        final String SQL_DELETE = "DELETE FROM product WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Deleting product failed, no rows affected.");
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting product", ex);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getBigDecimal("price"))
                .brand(rs.getString("brand"))
                .alcoholType(AlcoholType.valueOf(rs.getString("alcohol_type")))
                .quantity(rs.getInt("quantity"))
                .alcoholContent(rs.getDouble("alcohol_content"))
                .capacityInMilliliters(rs.getInt("capacity_in_milliliters"))
                .imagePath(rs.getString("image_path"))
                .build();
    }

    private String stringSearchBuilder(ProductSearchDto productSearch){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" WHERE ");

        stringBuilder.append("capacity_in_milliliters BETWEEN ");
        stringBuilder.append(productSearch.getMinCapacity());
        stringBuilder.append(" AND ");
        stringBuilder.append(productSearch.getMaxCapacity());

        stringBuilder.append(" AND ");
        stringBuilder.append("alcohol_content BETWEEN ");
        stringBuilder.append(productSearch.getMinAlcoholContent());
        stringBuilder.append(" AND ");
        stringBuilder.append(productSearch.getMaxAlcoholContent());

        stringBuilder.append(" AND ");
        stringBuilder.append("price BETWEEN ");
        stringBuilder.append(productSearch.getMinPrice());
        stringBuilder.append(" AND ");
        stringBuilder.append(productSearch.getMaxPrice());

        if(!productSearch.getAlcoholType().isEmpty()){
            stringBuilder.append(" AND alcohol_type IN (");
            for (AlcoholType type : productSearch.getAlcoholType()) {
                stringBuilder.append("'").append(type.name()).append("'");
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(") ");
        }

        if(productSearch.getName() != null && !productSearch.getName().isEmpty()){
            stringBuilder.append("AND name LIKE %?%");
        }

        if (productSearch.getBrand() != null && !productSearch.getBrand().isEmpty()){
            stringBuilder.append("AND brand LIKE %?%");
        }

        return stringBuilder.toString();
    }
}
package service.product;

import model.Product;
import service.MySQLConnUtils;
import service.MySQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements IProductService {
    private static final String SELECT_PRODUCT_BY_ID = "select * from product where id = ?";
    private static final String SELECT_ALL_PRODUCTS = "select * from product";
    private static final String ADD_NEW_PRODUCT = "insert into product (name, price, quantity, color, description, category) value (?,?,?,?,?,?);";
    private static final String DELETE_PRODUCT = "delete from product where id = ?;";
    private static final String UPDATE_PRODUCT = "update product set name = ?,price = ?, quantity = ?, color = ?, description = ?, category = ? where id = ?;";
    private static final String FIND_PRODUCT_BY_NAME = "select * from product where name like ?;";

    public ProductService() {
    }

    @Override
    public List<Product> getProductList() {
        List<Product> gameList = new ArrayList<>();

        try (Connection connection = MySQLConnUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                gameList.add(getProductInfo(resultSet, id));
            }
        }
        catch (SQLException e) {
            MySQLException.printSQLException(e);
        }
        return gameList;
    }

    @Override
    public void creatNewProduct(Product product) {
        try (Connection connection = MySQLConnUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(ADD_NEW_PRODUCT)) {
            setProductInfo(statement, product);
            statement.executeUpdate();
        } catch (SQLException e) {
            MySQLException.printSQLException(e);
        }
    }

    @Override
    public Product findProductById(int id) {
        Product product = null;
        try (Connection connection = MySQLConnUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                product = getProductInfo(resultSet, id);
            }
        }
        catch (SQLException e) {
            MySQLException.printSQLException(e);
        }
        return product;
    }

    @Override
    public List<Product> findProductByName(String input) {
        String findingValue = "%" + input + "%";
        List<Product> gameList = new ArrayList<>();

        try (Connection connection = MySQLConnUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCT_BY_NAME)) {
            preparedStatement.setString(1, findingValue);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                gameList.add(getProductInfo(resultSet, id));
            }
        }
        catch (SQLException e) {
            MySQLException.printSQLException(e);
        }
        return gameList;
    }

    @Override
    public boolean updateProduct(int id, Product product) {
        boolean productUpdated = false;
        try (Connection connection = MySQLConnUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT);) {
            setProductInfo(statement, product);
            statement.setInt(7, id);
            productUpdated = statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            MySQLException.printSQLException(exception);
        }
        return productUpdated;

    }

    @Override
    public boolean deleteProduct(int id) {
        boolean productRemoved = false;
        try (Connection connection = MySQLConnUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT);) {
            statement.setInt(1, id);
            productRemoved = statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            MySQLException.printSQLException(exception);
        }
        return productRemoved;

    }

    private Product getProductInfo (ResultSet resultSet, int id) throws SQLException {
        String name = resultSet.getString("name");
        double price = Double.parseDouble(resultSet.getString("price"));
        int quantity = Integer.parseInt(resultSet.getString("quantity"));
        String color = resultSet.getString("color");
        String description = resultSet.getString("description");
        String category = resultSet.getString("category");

        return new Product(id, name, price, quantity, color, description, category);
    }

    private void setProductInfo(PreparedStatement statement, Product product) throws SQLException {
        statement.setString(1, product.getName());
        statement.setDouble(2, product.getPrice());
        statement.setInt(3, product.getQuantity());
        statement.setString(4, product.getColor());
        statement.setString(5, product.getDescription());
        statement.setString(6, product.getCategory());

    }
}

package service.product;

import model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getProductList();

    void creatNewProduct(Product product);

    Product findProductById(int id);

    List<Product> findProductByName(String name);

    boolean updateProduct(int id, Product product);

    boolean deleteProduct(int id);
}

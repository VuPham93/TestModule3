package controller;

import model.Product;
import service.product.IProductService;
import service.product.ProductService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = "/productServlet")
public class ProductServlet extends HttpServlet {
    private final IProductService productService = new ProductService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");

        if(action == null){
            action = "";
        }

        switch (action){
            case "new":
                createNewProduct(request, response);
                break;
            case "edit":
                updateProductInfo(request, response);
                break;
            case "delete":
                removeProduct(request, response);
                break;
            case "find":
                findProduct(request, response);
                break;
            default:
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");

        if(action == null){
            action = "";
        }
        switch (action){
            case "new":
                showNewProductForm(request, response);
                break;
            case "edit":
                showUpdateProductInfoForm(request, response);
                break;
            case "delete":
                showRemoveProductForm(request, response);
                break;
            case "view":
                showProductDetail(request, response);
                break;
            default:
                showProductList(request, response);
                break;
        }
    }

    private void showProductList(HttpServletRequest request, HttpServletResponse response) {
        List<Product> productList = productService.getProductList();

        request.setAttribute("productList", productList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("view/productView/productList.jsp");

        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showProductDetail(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findProductById(id);

        RequestDispatcher dispatcher;

        if (product == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            request.setAttribute("product", product);
            dispatcher = request.getRequestDispatcher("view/productView/view.jsp");
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void findProduct(HttpServletRequest request, HttpServletResponse response) {
        String input = request.getParameter("findingProduct");
        List<Product> products = productService.findProductByName(input);
        request.setAttribute("productList", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/productView/productList.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showNewProductForm(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/productView/create.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewProduct(HttpServletRequest request, HttpServletResponse response) {
        List<Product> productList = productService.getProductList();

        int id = productList.get(productList.size() - 1).getId() + 1;
        Product product = getInfo(request, id);
        productService.creatNewProduct(product);

        RequestDispatcher dispatcher = request.getRequestDispatcher("view/productView/create.jsp");
        request.setAttribute("message", "New product was added to library");

        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateProductInfoForm(HttpServletRequest request, HttpServletResponse response) {
        showForm(request, response,"view/productView/update.jsp", productService);
    }

    private void updateProductInfo(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = getInfo(request, id);
        productService.updateProduct(id, product);

        request.setAttribute("message", "Product information was updated");
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/productView/update.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showRemoveProductForm(HttpServletRequest request, HttpServletResponse response) {
        showForm(request, response,"view/productView/remove.jsp", productService);
    }

    private void removeProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        productService.deleteProduct(id);
        try {
            response.sendRedirect("productServlet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Product getInfo(HttpServletRequest request, int id) {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        return new Product(id, name, price, quantity, color, description, category);
    }

    private static void showForm(HttpServletRequest request, HttpServletResponse response, String path, IProductService productService) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findProductById(id);

        RequestDispatcher dispatcher;

        if (product == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            request.setAttribute("product", product);
            dispatcher = request.getRequestDispatcher(path);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}

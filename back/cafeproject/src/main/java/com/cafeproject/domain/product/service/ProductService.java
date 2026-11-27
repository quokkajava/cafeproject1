package com.cafeproject.domain.product.service;

import com.cafeproject.domain.product.controller.AdminProductController;
import com.cafeproject.domain.product.controller.ProductController;
import com.cafeproject.domain.product.dto.CustomerProductDTO;
import com.cafeproject.domain.product.dto.ProductRequestDTO;
import com.cafeproject.domain.product.entity.Product;
import com.cafeproject.domain.product.repository.ProductRepository;
import com.cafeproject.global.globalExceptionHandler.BusinessException;
import com.cafeproject.global.globalExceptionHandler.ProductError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public long count() {
        return productRepository.count();
    }

    public Product create(ProductRequestDTO dto) {
        Product post = new Product(dto);
        return productRepository.save(post);
    }

    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new BusinessException(ProductError.NOT_FOUND);
        }

        return product.get();
    }

    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public void delete(Product post) {
        productRepository.delete(post);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public void flush() {
        productRepository.flush();
    }

    public List<Product> findByOnSale(boolean onSale) {
        return productRepository.findByOnSale(onSale);
    }

    /**
     * admin product 수정
     * 트랜잭션으로 save... 명시적으로 save 호출하지않음.
     * @param {Product} product
     * @param {AdminProductController.PostModifyReqBody} reqBody
     */
    public void modify(Product product, AdminProductController.PostModifyReqBody reqBody) {
        if (reqBody.name() != null && !reqBody.name().isEmpty()) {
            product.setName(reqBody.name());
        }
        if (reqBody.origin() != null && !reqBody.origin().isEmpty()) {
            product.setOrigin(reqBody.origin());
        }
        if (reqBody.description() != null && !reqBody.description().isEmpty()) {
            product.setDescription(reqBody.description());
        }
        if (reqBody.imageUrl() != null && !reqBody.imageUrl().isEmpty()) {
            product.setImageUrl(reqBody.imageUrl());
        }
        if (reqBody.price() != null && reqBody.price() > 0) {
            product.setPrice(reqBody.price());
        }
        if (reqBody.onSale() != null) {
            product.setOnSale(reqBody.onSale());
        }
    }
    public void modify(Product product, boolean onSale) {
        product.setOnSale(onSale);
    }

    public void truncate() {
        productRepository.truncateProductTable();
    }

    public Product findLatest() {
        Optional<Product> product = productRepository.findFirstByOrderByIdDesc();
        if(product.isEmpty()){
            throw new BusinessException(ProductError.NOT_FOUND);
        }

        return product.get();
    }

    public CustomerProductDTO findOnSaleById(long id) {
        Optional<Product> product = productRepository.findByIdAndOnSaleTrue(id);
        if (product.isEmpty()){
            throw new BusinessException(ProductError.NOT_FOUND);
        }
        Product _product = product.get();
        return new CustomerProductDTO(_product);
    }
}

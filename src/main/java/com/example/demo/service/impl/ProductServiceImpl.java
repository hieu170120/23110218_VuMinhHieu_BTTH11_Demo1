package com.example.demo.service.impl;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service

public class ProductServiceImpl implements ProductService {
	    
	 @Autowired
	    private ProductRepository repo;

	    public ProductServiceImpl(ProductRepository repo) {
	        this.repo = repo;
	    }

	    @Override
	    public List<Product> listAll() {
	        return repo.findAll();
	    }

	    @Override
	    public Product save(Product product) {
	        return repo.save(product);
	    }

	    @Override
	    public Product get(Long id) {
	        return repo.findById(id).get();
	    }

	    @Override
	    public void delete(Long id) {
	        repo.deleteById(id);
	    }

		@Override
		public List<Product> getAll() {
			// TODO Auto-generated method stub
			return null;
		}

}

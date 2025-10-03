package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Product;
import org.springframework.stereotype.Service;
@Service

public interface ProductService {
		void delete (Long id);
		Product get (Long id);
		Product save (Product product);
		List<Product> getAll();
		List<Product> listAll();
}

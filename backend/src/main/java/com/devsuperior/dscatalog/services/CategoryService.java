package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();

		// List<CategoryDTO> listDTO = list.stream().map(x -> new
		// CategoryDTO(x)).collect(Collectors.toList());

		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("CV:Entity not found"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try  {
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}
		catch ( EntityNotFoundException e) {
			throw new ResourceNotFoundException("CV: Id not found " + id);
		}
		
	}

	public void delete(Long id) {
			try {
			repository.deleteById(id);
			}
			catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException("CV - Id not found " + id);
			}
			catch (DataIntegrityViolationException e) {
				throw new DatabaseException("CV - Integrity violation");
			}
		
		
	}

	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		//return null;
		
		//List<Category> list = repository.findAll();
		Page<Category> list = repository.findAll(pageRequest);

		// List<CategoryDTO> listDTO = list.stream().map(x -> new
		// CategoryDTO(x)).collect(Collectors.toList());

		//return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		return list.map(x -> new CategoryDTO(x));

	}

	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));

	}
	

}
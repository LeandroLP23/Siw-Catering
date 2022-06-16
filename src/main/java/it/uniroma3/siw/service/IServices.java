package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface IServices<T> {
    
    public void save(T o);
    
    public T findById(Long id);
    
    public List<T> findAll();
    
    public boolean alreadyExists(T o);
    
    public void deleteById(Long id);

}

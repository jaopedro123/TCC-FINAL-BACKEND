package com.MotherBoard.Admin.marca;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.MotherBoard.entidade.comum.Marca;

@Service
public class MarcaServico {

	static final int MARCAS_PER_PAGE = 1;
	
	
	@Autowired
	private MarcaRepository mRepository;
	
	public List<Marca> listAll() {
		return (List<Marca>) mRepository.findAll();
	}

	public Marca get(Integer id) throws MarcaNotFoundException {
	    try {
	        return mRepository.findById(id).get();
	    } 
	    catch (NoSuchElementException ex) {
	        throw new MarcaNotFoundException("Nao foi possivel achar a marca com ID " + id);
	    }
	}

	public void delete(Integer id) throws MarcaNotFoundException {
	    if (!mRepository.existsById(id)) {
	        throw new MarcaNotFoundException("A marca com ID " + id + " não foi encontrada.");
	    }
	    
	    mRepository.deleteById(id);
	}

	

    public boolean checkUniqueMarca(Integer id, String nome) {
        Marca marcaByNome = mRepository.getMarcaBynome(nome);
        if (marcaByNome == null) return true;

        return id != null && marcaByNome.getId().equals(id);
    }
    
    
    
    public Page<Marca> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, MARCAS_PER_PAGE, sort);
        
        if (keyword != null && !keyword.isEmpty()) {
            return mRepository.findAllBynomeContainingMarca(keyword, pageable);
        }
        return mRepository.findAll(pageable);
    }

    public Marca save(Marca marca) {
        return mRepository.save(marca);
    }

	
	public void updateMarcaStatus(Integer id, boolean habilitado) {
		mRepository.updateMarcaStatus(id, habilitado);
    }


	
}

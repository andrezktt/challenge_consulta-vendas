package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> searchReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate min = minDate.isEmpty() ? today.minusDays(365) : LocalDate.parse(minDate);
		LocalDate max = maxDate.isEmpty() ? today : LocalDate.parse(maxDate);
		Page<Sale> saleReport = repository.searchSales(min, max, name, pageable);
		repository.searchSalesWithSellers(saleReport.getContent());
		return saleReport.map(SaleReportDTO::new);
	}

	public List<SaleSummaryDTO> searchSummary(String minDate, String maxDate) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate min = minDate.isEmpty() ? today.minusDays(365) : LocalDate.parse(minDate);
		LocalDate max = maxDate.isEmpty() ? today : LocalDate.parse(maxDate);
		return repository.searchSummary(min, max);
	}
}

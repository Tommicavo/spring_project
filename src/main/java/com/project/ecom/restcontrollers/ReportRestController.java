package com.project.ecom.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.dtos.ReportDto;
import com.project.ecom.models.entities.ReportEntity;
import com.project.ecom.services.ReportService;
import com.project.ecom.utils.AppUtils;

@RestController
@RequestMapping("/reports")
public class ReportRestController {
	
	@Autowired
	private ReportService reportService;

	@Autowired
	private AppUtils utils;

	@GetMapping()
	@PreAuthorize("isAuthenticated()")
	@Cacheable(value = "reports", key = "'all_reports'")
	public ResponseEntity<?> getAllReports() {
		try {
			List<ReportEntity> reports = reportService.getAllReports();
			if (reports == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getAllReports", "Reports are null"));
			}
			return ResponseEntity.ok().body(reports);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getAllReports", "Exception in getAllReports: " + e.getMessage()));
		}
	}

	@GetMapping("/{idReport}")
	@PreAuthorize("isAuthenticated()")
	@Cacheable(value = "reports", key = "#idReport")
	public ResponseEntity<?> getReport(@PathVariable Long idReport) {
		try {
			ReportEntity report = reportService.getReport(idReport);
			if (report == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getReport", "Report is null"));
			}
			return ResponseEntity.ok().body(report);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getReport", "Exception in getReport: " + e.getMessage()));
		}
	}

	@PostMapping()
	@PreAuthorize("isAuthenticated()")
	@CachePut(value = "reports", key = "#result.body.idReport")
	public ResponseEntity<?> insertReport(@RequestBody ReportDto reportDto) {
		try {
			Long idUser = utils.getIdUserFromSCH(SecurityContextHolder.getContext().getAuthentication());
			ReportEntity report = reportService.insertReport(idUser, reportDto);
			if (report == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("insertReport", "Report is null"));
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(report);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("insertReport", "Exception in insertReport: " + e.getMessage()));
		}
	}

	@PostMapping("/get-pdf/{idReport}")
	@PreAuthorize("isAuthenticated()")
	@CachePut(value = "reports", key = "#result.body.idReport")
	public ResponseEntity<?> getReportPdf(@PathVariable Long idReport) {
		try {
			ReportEntity report = reportService.getReportPdf(idReport);
			if (report == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getReportPdf", "Report not found"));
			}
			return ResponseEntity.ok().body(report);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getReportPdf", "Exception: " + e.getMessage()));
		}
	}
}

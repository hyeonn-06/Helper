package com.helper.policy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helper.page.util.PageObject;
import com.helper.policy.mapper.PolicyMapper;
import com.helper.policy.vo.PolicyVO;

@Service
public class PolicyService {

	@Autowired
	private PolicyMapper mapper;
	
	public List<PolicyVO> policyList(PageObject pageObject) {
		pageObject.setTotalRow(mapper.getTotalRow(pageObject));
		return mapper.policyList(pageObject);
	}
	
}

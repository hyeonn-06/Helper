package com.helper.policy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.helper.page.util.PageObject;
import com.helper.policy.vo.PolicyVO;

@Mapper
public interface PolicyMapper {

	public Long getTotalRow(PageObject pageObject);
	
	public List<PolicyVO> policyList(PageObject pageObject);
	
}

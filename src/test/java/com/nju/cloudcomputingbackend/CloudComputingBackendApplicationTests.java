package com.nju.cloudcomputingbackend;

import com.nju.cloudcomputingbackend.model.HottestUniversityList;
import com.nju.cloudcomputingbackend.service.APIService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudComputingBackendApplicationTests {

	@Autowired
	private APIService apiService;

	@Test
	public void testGetHottestUniversityByMonth() {
		HottestUniversityList res = apiService.getHottestUniversityByMonth("2019-08");
		Assert.assertEquals("2019-08", res.getTime());
		Assert.assertEquals("南京大学", res.getNameList().get(0));
		assert(848 == res.getRankList().get(0));
	}

}

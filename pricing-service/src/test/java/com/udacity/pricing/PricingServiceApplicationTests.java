package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.closeTo;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PricingServiceApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JacksonTester<Price> json;

	@MockBean
	private PricingService service;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPrice() throws Exception {
		Price price = new Price("USD", BigDecimal.valueOf(11790.45), 1L);
		given(service.getPrice(any())).willReturn(price);

		mvc.perform(
				get(new URI("/pricing-service?vehicleId=1")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currency", is(price.getCurrency())))
				.andExpect(jsonPath("$.price").value(closeTo(11790.45, 0.1)))
				.andExpect(jsonPath("$.vehicleId", is(1)));
		verify(service, times(1)).getPrice(any(Long.class));
	}


}

package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@AutoConfigureJsonTesters
public class PricingServiceIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

//	@Autowired
//	private MockMvc mvc;
//
//	@Autowired
//	private JacksonTester<Price> json;
//
//	@MockBean
//	private PricingService service;

	@Test
	public void contextLoads() {
	}

//	@Test
//	public void getPrice() throws Exception {
//		Price price = new Price("USD", BigDecimal.valueOf(11790.45), 1L);
//		given(service.getPrice(any())).willReturn(price);
//
//		mvc.perform(
//				get(new URI("/pricing-service?vehicleId=1")))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.currency", is(price.getCurrency())))
//				.andExpect(jsonPath("$.price").value(closeTo(11790.45, 0.1)))
//				.andExpect(jsonPath("$.vehicleId", is(1)));
//		verify(service, times(1)).getPrice(any(Long.class));
//	}

	@Test
	public void priceIntegrationTesting() {
		ResponseEntity<Price> response = restTemplate.getForEntity("http://localhost:" + port + "/pricing-service?vehicleId=1", Price.class);
		System.out.println(response.toString());

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}

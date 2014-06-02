package de.hsbremen.kss.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import static org.easymock.EasyMock.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

public class RandomUtilsTest {

	RandomUtils randomUtils;
	
	@Before
	public void test() {
		this.randomUtils = new RandomUtils(0);
	}

	@Test
	public void testRandomElementByLinearDistribution() {
		List<Integer> elements = Arrays.asList(4, 5, 1,2,3);
		
		this.randomUtils = createMockBuilder(RandomUtils.class).addMockedMethod("nextInt").createMock();
		
		for (int i = 0; i< 15; i++) {
			expect(this.randomUtils.nextInt(0, 15)).andReturn(i);
		}
		
		replay(this.randomUtils);


		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(4)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(4)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(4)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(4)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(4)));

		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(5)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(5)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(5)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(5)));
		
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(1)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(1)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(1)));
		
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(2)));
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(2)));
		
		assertThat(this.randomUtils.randomElementByLinearDistribution(elements), equalTo(Integer.valueOf(3)));
	}
	
}

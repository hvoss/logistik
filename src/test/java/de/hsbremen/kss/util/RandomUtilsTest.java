package de.hsbremen.kss.util;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        final List<Integer> elements = Arrays.asList(4, 5, 1, 2, 3);

        this.randomUtils = createMockBuilder(RandomUtils.class).addMockedMethod("nextInt").createMock();

        for (int i = 0; i < 15; i++) {
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

    @Test
    public void testRandomSublist() {
        final List<Integer> elements = Arrays.asList(4, 5, 1, 2, 3);

        this.randomUtils = createMockBuilder(RandomUtils.class).addMockedMethod("nextInt").createMock();

        expect(this.randomUtils.nextInt(0, 5)).andReturn(0);
        expect(this.randomUtils.nextInt(1, 6)).andReturn(2);

        expect(this.randomUtils.nextInt(0, 5)).andReturn(4);
        expect(this.randomUtils.nextInt(5, 6)).andReturn(5);

        replay(this.randomUtils);

        assertThat(this.randomUtils.randomSublist(elements), equalTo(Arrays.asList(4, 5)));
        assertThat(this.randomUtils.randomSublist(elements), equalTo(Arrays.asList(3)));
    }

    @Test
    public void testRemoveRandomSublist() {
        final List<Integer> elements = new ArrayList<>(Arrays.asList(4, 5, 1, 2, 3));

        this.randomUtils = createMockBuilder(RandomUtils.class).addMockedMethod("nextInt").createMock();

        expect(this.randomUtils.nextInt(0, 5)).andReturn(0);
        expect(this.randomUtils.nextInt(1, 6)).andReturn(2);

        expect(this.randomUtils.nextInt(0, 3)).andReturn(1);
        expect(this.randomUtils.nextInt(2, 4)).andReturn(2);

        replay(this.randomUtils);

        assertThat(this.randomUtils.removeRandomSublist(elements), equalTo(Arrays.asList(4, 5)));
        assertThat(elements, equalTo(Arrays.asList(1, 2, 3)));
        assertThat(this.randomUtils.removeRandomSublist(elements), equalTo(Arrays.asList(2)));
        assertThat(elements, equalTo(Arrays.asList(1, 3)));
    }

}

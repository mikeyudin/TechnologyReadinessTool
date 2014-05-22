package net.techreadiness.rules;

import java.util.List;

import net.techreadiness.service.exception.ServiceException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.builder.conf.EvaluatorOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestStartsWithOperator {
	private static KnowledgeBase knowlegeBase;
	private StatefulKnowledgeSession session;
	private List<String> toTest;
	private List<String> startedWith;
	private List<String> startedWithNull;
	private List<String> nullStartedWith;
	private List<String> notStartsWith;

	@BeforeClass
	public static void beforeClass() {
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setOption(EvaluatorOption.get("startsWith", new StartsWithEvaluatorDefinition()));

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(config);
		kbuilder.add(ResourceFactory.newClassPathResource("net/techreadiness/rules/starts-with.drl"), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			throw new ServiceException("Problem building cross field validation rules.");
		}

		knowlegeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowlegeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());

	}

	@Before
	public void before() {
		session = knowlegeBase.newStatefulKnowledgeSession();
		startedWith = Lists.newArrayList();
		session.setGlobal("startedWith", startedWith);
		startedWithNull = Lists.newArrayList();
		session.setGlobal("startedWithNull", startedWithNull);
		nullStartedWith = Lists.newArrayList();
		session.setGlobal("nullStartedWith", nullStartedWith);
		notStartsWith = Lists.newArrayList();
		session.setGlobal("notStartsWith", notStartsWith);
		toTest = Lists.newArrayList();
	}

	@After
	public void after() {
		session.dispose();
	}

	@Test
	public void startsWithPositive() {
		toTest.add("hello name");
		toTest.add("helloname");
		toTest.add("hello ");
		toTest.add("hello");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();

		for (String string : toTest) {
			Assert.assertTrue(string + ", starts with 'hello'.", startedWith.contains(string));
		}
	}

	@Test
	public void startsWithNegative() {
		toTest.add("helo name");
		toTest.add(" hello");
		toTest.add("ello");
		toTest.add("heloname");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();

		for (String string : toTest) {
			Assert.assertFalse(string + ", does not start with 'hello'.", startedWith.contains(string));
		}
	}

	@Test
	public void startsWithNull() {
		toTest.add("helo name");
		toTest.add(" hello");
		toTest.add("null");
		toTest.add("heloname");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();

		for (String string : toTest) {
			Assert.assertFalse(string + ", does not start with 'hello'.", startedWithNull.contains(string));
		}
	}

	@Test
	public void notStartsWith() {
		toTest.add("helo name");
		toTest.add(" hello");
		toTest.add("null");
		toTest.add("heloname");
		for (String string : toTest) {
			session.insert(string);
		}

		session.fireAllRules();

		for (String string : toTest) {
			Assert.assertTrue(string + ", does not start with 'hello'.", notStartsWith.contains(string));
		}
	}

	@Test
	public void nullStartsWith() {
		session.insert(null);

		session.fireAllRules();

		Assert.assertFalse("null, does not start with 'hello'.", !nullStartedWith.isEmpty());
	}
}

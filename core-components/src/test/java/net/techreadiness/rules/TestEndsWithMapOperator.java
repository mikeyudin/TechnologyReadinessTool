package net.techreadiness.rules;

import java.util.List;
import java.util.Map;

import net.techreadiness.service.exception.ServiceException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.builder.conf.EvaluatorOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestEndsWithMapOperator {
	private static KnowledgeBase knowlegeBase;

	@BeforeClass
	public static void beforeClass() {
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setOption(EvaluatorOption.get("endsWith", new EndsWithEvaluatorDefinition()));

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(config);
		kbuilder.add(ResourceFactory.newClassPathResource("net/techreadiness/rules/ends-with-map.drl"), ResourceType.DRL);

		if (kbuilder.hasErrors()) {
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			throw new ServiceException("Problem building cross field validation rules: " + errors);
		}

		knowlegeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowlegeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	}

	@Test
	public void endsWithPositive() {
		StatefulKnowledgeSession session = knowlegeBase.newStatefulKnowledgeSession();
		List<String> endedWith = Lists.newArrayList();
		session.setGlobal("endedWith", endedWith);
		List<Map<String, String>> toTest = Lists.newArrayList();
		Map<String, String> helloName = Maps.newHashMap();
		helloName.put("code", "name hello");
		toTest.add(helloName);

		for (Map<String, String> map : toTest) {
			session.insert(map);
		}

		session.fireAllRules();
		session.dispose();

		for (Map<String, String> map : toTest) {
			Assert.assertTrue(map.get("code") + ", ends with 'hello'.", endedWith.contains(map.get("code")));
		}
	}

	@Test
	public void endsWithNegative() {
		StatefulKnowledgeSession session = knowlegeBase.newStatefulKnowledgeSession();
		List<String> endedWith = Lists.newArrayList();
		session.setGlobal("endedWith", endedWith);
		List<Map<String, String>> toTest = Lists.newArrayList();
		Map<String, String> helloName = Maps.newHashMap();
		helloName.put("code", "name helo");
		toTest.add(helloName);

		for (Map<String, String> map : toTest) {
			session.insert(map);
		}

		session.fireAllRules();
		session.dispose();

		for (Map<String, String> map : toTest) {
			Assert.assertFalse(map.get("code") + ", does not end with 'hello'.", endedWith.contains(map.get("code")));
		}
	}
}

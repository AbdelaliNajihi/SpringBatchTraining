package com.example.batch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.example.batch.entity.User;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, 
			ItemReader<User> itemReader, ItemWriter<User> itemWriter, ItemProcessor<User, User> itemProcessor) {
		System.out.println("STEP");
		Step step = stepBuilderFactory.get("step")
					.<User, User>chunk(1)
					.reader(itemReader)
					.processor(itemProcessor)
					.writer(itemWriter)
					.build();
		System.out.println("JOB");
		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).start(step).build();
	}
	
	/*@Bean
	public FlatFileItemReader<User> flatFileItemReader(){
		FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource("src/main/resources/users.csv"));
		flatFileItemReader.setName("CSV-reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}*/
	
	@Bean
	public StaxEventItemReader<User> staxEventItemReader(){
		System.out.println("START reading from xml file");
		return new StaxEventItemReaderBuilder<User>().name("itemReader")
				.resource(new ClassPathResource("data.xml"))
				.addFragmentRootElements("user")
				.unmarshaller(userMarshaller())
				.build();
	}
	
	@Bean
	public XStreamMarshaller userMarshaller() {
		System.out.println("Marshaller");
		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		Map<String, Class> aliases = new HashMap<>();
		aliases.put("user", User.class);
		aliases.put("userId", Long.class);
		aliases.put("name", String.class);
		aliases.put("emailAddress", String.class);
		aliases.put("purchasedPackage", String.class);
		xStreamMarshaller.setAliases(aliases);
		return xStreamMarshaller;
	}
	
	/*@Bean
	public JsonItemReader<User> jsonItemReader(){
		System.out.println("START reading from json file");
		//ObjectMapper objectMapper = new ObjectMapper();
		JacksonJsonObjectReader<User> jacksonJsonObjectReader = new JacksonJsonObjectReader<>(User.class);
		//jacksonJsonObjectReader.setMapper(objectMapper);
		return new JsonItemReaderBuilder<User>().jsonObjectReader(jacksonJsonObjectReader)
				.resource(new ClassPathResource("data.json"))
				.name("UserJsonItemReader")
				.build();
	}*/

	/*@Bean
	public LineMapper<User> lineMapper(){
		DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setNames(new String [] {"userId", "name", "dept", "salary"});
		beanWrapperFieldSetMapper.setTargetType(User.class);
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		
		return defaultLineMapper;
	}*/
	
}

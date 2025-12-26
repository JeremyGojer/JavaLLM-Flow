package com.llmjava.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.llmjava.utils.SchemaGenerator;

//model	model	String	Required. The model name (e.g., llama3:8b).
//prompt	prompt	String	The text prompt for generation.
//stream	stream	Boolean	Crucial toggle. Default is true. Set to false for non-streaming responses.
//options	options	Options	Inner DTO for temperature, top_k, etc. (See Day 3's task).
//format	format	String	For requesting structured output (e.g., "json").
//system	system	String	System message to override the Modelfile.
//context   context to be sent to the model
public abstract class AbstractOllamaRequest {
    private final String model;
    private final Boolean stream;
    private final Options options;
    private final Object format;
    @SerializedName("keep_alive")
    private final String keepAlive;
    private final String system;
    private final Boolean raw;
    private final List<String> images;

    protected AbstractOllamaRequest(Builder<?, ?> builder) {
        this.model = builder.model;
        this.stream = builder.stream;
        this.options = builder.options;
        this.format = builder.format;
        this.keepAlive = builder.keepAlive;
        this.system = builder.system;
        this.raw = builder.raw;
        this.images = builder.images;
    }

    

    public String getModel() {
		return model;
	}



	public Boolean getStream() {
		return stream;
	}



	public Options getOptions() {
		return options;
	}



	public Object getFormat() {
		return format;
	}



	public String getKeepAlive() {
		return keepAlive;
	}


	public String getSystem() {
		return system;
	}



	public Boolean getRaw() {
		return raw;
	}



	public List<String> getImages() {
		return images;
	}





	public abstract static class Builder<T extends Builder<T, R>, R extends AbstractOllamaRequest> {
        protected String model;
        protected Boolean stream = true;
        protected Options options;
        protected Object format;
        protected String keepAlive;
        protected String system;
    	protected Boolean raw;
    	protected List<String> images;

        protected abstract T self();

        public abstract R build();

        public T model(String model) {
            this.model = model;
            return self();
        }

        public T stream(Boolean stream) {
            this.stream = stream;
            return self();
        }

        public T options(Options options) {
            this.options = options;
            return self();
        }
        
        public T format(String format) {
            this.format = format;
            return self();
        }
        
        public T keepAlive(String keepAlive) {
            this.keepAlive = keepAlive;
            return self();
        }
        
        public T system(String system) {
            this.system = system;
            return self();
        }
        
        public T raw(Boolean raw) {
            this.raw = raw;
            return self();
        }
        
        public T images(List<String> images) {
            this.images = images;
            return self();
        }
        
        public T formatAs(Class<?> clazz) {
            this.format = SchemaGenerator.generate(clazz); 
            return self();
        }
    }
}

from transformers import AutoModelForCausalLM, AutoTokenizer

class TextGenerationInference:
    default_model = "HuggingFaceTB/SmolLM2-1.7B-Instruct"

    def __init__(self, model = default_model):
        self.model = model

    def inference(self, system_prompt, assistant_prompt, user_prompt) -> string:
        if (self.isStringBlank(user_prompt)):
            raise ValueError("user prompt is blank")

        prompts = self.prepare_prompts(system_prompt, assistant_prompt, user_prompt)
        tokenizer = AutoTokenizer.from_pretrained(self.model, trust_remote_code=True)
        model = AutoModelForCausalLM.from_pretrained(self.model, device_map="auto", trust_remote_code=True)
        inputs = tokenizer.apply_chat_template(prompts, add_generation_prompt=True, return_tensors='pt').to(model.device)
        outputs = model.generate(inputs, max_new_tokens=1024, temperature=0.8, do_sample=True)

        return tokenizer.decode(outputs[0])

    def isStringBlank(self, string) -> bool:
        return not (string and string.isspace())

    def isStringNotBlank(self, string) -> bool:
         return bool(string and not string.isspace())

    def prepare_prompts(self, system_prompt, assistant_prompt, user_prompt) -> dict:
        messages = []
        if (self.isStringNotBlank(system_prompt)):
            messages.append({"role": "system", "content": system_prompt})
        if (self.isStringNotBlank(assistant_prompt)):
            messages.append({"role": "assistant", "content": assistant_prompt})
        messages.append({"role": "user", "content": user_prompt})
        return messages

result = TextGenerationInference().inference(sys_prompt, asst_prompt, usr_prompt)
result
/* Mostrar o conteúdo dos botões */
const divs = document.querySelectorAll('.conteudo')

function mostrarDiv(id) {
  document.getElementById('dadosConta').style.display = "none"
  const div = document.getElementById(id);


  for (let i = 0; i < divs.length; i++) {
    divs[i].style.display = "none";
  }

  if(div) {
    div.style.display = "block"
  }
}

/* fim - Mostrar o conteúdo dos botões */

/* Botão ativo e inativo */
const dadosBtn = document.getElementById('dadosBtn')
const pedidosBtn = document.getElementById('pedidosBtn')
const enderecoBtn = document.getElementById('enderecoBtn')

function dadosActive() {
  pedidosBtn.classList.remove('active');
  enderecoBtn.classList.remove('active');

  dadosBtn.classList.add('active');
}

function pedidosActive() {
  dadosBtn.classList.remove('active');
  enderecoBtn.classList.remove('active');

  pedidosBtn.classList.add('active');
}

function enderecoActive() {
  dadosBtn.classList.remove('active');
  pedidosBtn.classList.remove('active');

  enderecoBtn.classList.add('active');
}
/* fim - Botão ativo e inativo */

/* Validação Dados */
var inputNomeCompleto = document.getElementById("nomeCompleto");
var botaoAtualizar = document.querySelector(".atualizaDados");
var erroNomeCompleto = document.getElementById("erroNomeCompleto");

erroNomeCompleto.classList.remove("erro-mensagem-nome");

inputNomeCompleto.addEventListener("input", function() {
	var valor = inputNomeCompleto.value;
	valor = valor.replace(/[^a-zA-ZÀ-ÖØ-öø-ÿ\s]/g, '');

	if (valor.length < 3) {
		valor = valor.substring(0, 3);
		erroNomeCompleto.textContent = "O seu nome deve ter pelo menos 3 caracteres.";
		erroNomeCompleto.classList.add("erro-mensagem-nome");
	} else {
		erroNomeCompleto.textContent = "";
		erroNomeCompleto.classList.remove("erro-mensagem-nome");
	}

	inputNomeCompleto.value = valor;
	botaoAtualizar.disabled = valor.length < 3;
	
});
var telefoneInput = document.getElementById("telefone");
var erroTelefone = document.getElementById("erroTelefone");

erroTelefone.classList.remove("erro-mensagem-telefone");

telefoneInput.addEventListener("input", function() {
	var telefone = this.value.replace(/\D/g, ""); 
	var maxLength = 11;

	if (telefone.length > maxLength) {
		telefone = telefone.slice(0, maxLength);
	}
	if (telefone.length <= 10) {
		telefone = telefone.replace(/(\d{2})(\d{0,4})(\d{0,4})/, "($1) $2-$3");
		erroTelefone.textContent = "O número de telefone deve ter pelo menos 11 dígitos.";
		erroTelefone.classList.add("erro-mensagem-telefone");
	} 
	else {
		erroTelefone.textContent = "";
		erroTelefone.classList.remove("erro-mensagem-telefone");
		telefone = telefone.replace(/(\d{2})(\d{0,5})(\d{0,4})/, "($1) $2-$3");
	}
if (event.inputType === 'deleteContentBackward' || event.inputType === 'deleteContentForward') {
}
	else {  
	this.value = telefone;
}
	botaoAtualizar.disabled = telefone.length < 15;
});
	
inputNomeCompleto.addEventListener("input", validarCampos);
telefoneInput.addEventListener("input", validarCampos);

function validarCampos() {
    var nomeCompleto = inputNomeCompleto.value.trim();
    var telefone = telefoneInput.value.replace(/\D/g, '');

    botaoAtualizar.disabled = nomeCompleto.length < 3 || telefone.length < 11;
}

/* fim - Validação Dados */

/* Validação Endereço */
const cep = document.getElementById('cep')
const numero = document.getElementById('numero')

const invalidFeed = document.getElementById('invalidFeed')

const btnBuscar = document.getElementById('cepButton')
btnBuscar.addEventListener('click', buscarEnderecoPorCep)

const enderecoForm = document.getElementById('enderecoForm')
enderecoForm.addEventListener("submit",  function (event) {

    if (!cep.validity.valid) {
      invalidar(cep, cepInv)
      event.preventDefault();
    }else if (cep.value.replace(/[^0-9]/g, '').length < 8) {
      invalidar(cep, cepInv)
      event.preventDefault();
    } else {
      validar(cep, cepInv)
    }

    if (!rua.validity.valid) {
      invalidar(rua, ruaInv)
      event.preventDefault();
    }else{
      validar(rua, ruaInv)
    }

    if (!bairro.validity.valid) {
      invalidar(bairro, bairroInv)
      event.preventDefault();
    }else{
      validar(bairro, bairroInv)
    }

    if (!cidade.validity.valid) {
      invalidar(cidade, cidadeInv)
      event.preventDefault();
    }else{
      validar(cidade, cidadeInv)
    }

    if(estado.value === 'Selecione seu Estado...') {
      invalidar(estado, estadoInv)
      event.preventDefault();
    }

    if (!numero.validity.valid) {
      invalidar(numero, numeroInv)
      event.preventDefault();
    }else{
      validar(numero, numeroInv)
    }

    if (!complemento.validity.valid) {
      invalidar(complemento, complementoInv)
      event.preventDefault();
    }else{
      validar(complemento, complementoInv)
    }

    if (!referencia.validity.valid) {
      invalidar(referencia, referenciaInv)
      event.preventDefault();
    }else{
      validar(referencia, referenciaInv)
    }

  },
  false,
);

function buscarEnderecoPorCep() {

    fetch(`https://viacep.com.br/ws/${cep.value}/json/`)
      .then(response => response.json())
      .then(data => {
        if (data.erro) {
          alert('CEP não encontrado. Tente novamente.');
        } else {
          document.getElementById('rua').value = data.logradouro;
          document.getElementById('bairro').value = data.bairro
          document.getElementById('cidade').value = data.localidade;
          document.getElementById('estado').value = data.uf;
          validarInputs(rua , ruaInv);
          validarInputs(bairro , bairroInv);
          validarInputs(cidade , cidadeInv);
          validarInputs(estado , estadoInv);
        }
      })
      .catch(error => {
        alert('Erro ao buscar o CEP:', error);
    });
}

function validarCep() {
    if (cep.value.length < 9 ) {
        invalidar(cep, cepInv);
    }else {
        validar(cep, cepInv)
    }
}

function mascararCep() {
  let cepValue = cep.value.replace(/\D/g, '');

  if(cepValue.length > 0) {
    cepValue = cepValue.replace(/(\d{3})(\d{3})$/, "$1-$2");
  }

  cep.value = cepValue;
}

cep.addEventListener('input', () => {
  mascararCep();
  validarCep();
});

numero.addEventListener('input', () => {
  validarInputs(numero, numeroInv);
  numero.value = numero.value.replace(/[^0-9]/g, '')
});

function validarInputs(id, idInv) {
  if(id.value === '') {
    invalidar(id, idInv)
  }else {
    validar(id, idInv)
  }
}

function validar(input, id) {
    input.classList.remove('is-invalid')
    input.classList.add('is-valid')
    id.style.display = 'none'
}

function invalidar(input, id) {
    input.classList.add('is-invalid')
    id.style.display = 'block';
}
/* fim - Validação Endereço */

const inputs = [cep, rua, bairro, cidade, estado, numero, complemento, referencia]
const inputsInv = [cepInv, ruaInv, bairroInv, cidadeInv, estadoInv, numeroInv, complementoInv, referenciaInv]

const lixeira = document.getElementById('lixeira')
lixeira.addEventListener('click', () => {

  inputs.forEach((input) => {
    input.classList.remove('is-valid')
    input.classList.remove('is-invalid')
  })

  inputsInv.forEach((input) => {
    input.style.display = 'none'
  })

})


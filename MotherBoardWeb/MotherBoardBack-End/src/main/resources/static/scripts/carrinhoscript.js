const frete1 = document.getElementById("frete1")
const frete2 = document.getElementById("frete2")
const frete3 = document.getElementById("frete3")
const freteResumo = document.getElementById("freteResumo")

const cep = document.getElementById('cep')

let calcFrete = calculateFreight(cep.value)
let freteEconomicaCalc = calcFrete - 3
let fretePadraoCalc = (calcFrete + 4)
let freteExpressCalc = (calcFrete + 13)

const invalidFeed = document.getElementById('invalidFeed')

const btnBuscar = document.getElementById('cepButton')
btnBuscar.addEventListener('click', buscarEnderecoPorCep)

function buscarEnderecoPorCep() {

    fetch(`https://viacep.com.br/ws/${cep.value}/json/`)
      .then(response => response.json())
      .then(data => {
        if (data.erro) {
          alert('CEP não encontrado. Tente novamente.');
        } else {
          let calcFrete = calculateFreight(cep.value)

          frete1.textContent = "Entrega Econômica R$ " + freteEconomicaCalc.toFixed(2)
          frete2.textContent = "Entrega Padrão R$ " + fretePadraoCalc.toFixed(2)
          frete3.textContent = "Entrega Express R$ " + freteExpressCalc.toFixed(2)
          /* freteResumo.textContent = "R$ " + fretePadraoCalc.toFixed(2) */
          radioAtivo()

        }
      })
      .catch(error => {
        alert('Erro ao buscar o CEP:', error);
    });

    atualizarTotalCompra()
}

function calculateFreight(cep) {
  return Math.random() * 20 + 5;
}

/* FRETES RADIOS ATIVO */
const radioFrete1 = document.getElementById("radioFrete1")
const radioFrete2 = document.getElementById("radioFrete2")
const radioFrete3 = document.getElementById("radioFrete3")

radioFrete1.addEventListener("click", function() {  
  const produtos = document.querySelectorAll('.produto');
  let total = 1;
  let total2 = 1;
  
  if (cep.value.length === 9) {
    freteResumo.textContent = "R$ " + freteEconomicaCalc.toFixed(2)
    buscarEnderecoPorCep()

    produtos.forEach(produto => {
      const preco = parseFloat(produto.getAttribute('data-preco'));
      const quantidade = parseInt(produto.getAttribute('data-quantidade'));
      total += preco * quantidade;
      total2 += preco * quantidade;
    });
  
    document.getElementById('aVista').textContent = (total + freteEconomicaCalc).toFixed(2)
  } else if(cep.value === '') {
      console.log("cep 1 vazio")
  }
})
radioFrete2.addEventListener("click", function() {
  const produtos = document.querySelectorAll('.produto');
  let total = 1;
  let total2 = 1;
  
  if (cep.value.length === 9) {
    freteResumo.textContent = "R$ " + fretePadraoCalc.toFixed(2)
    buscarEnderecoPorCep()

    produtos.forEach(produto => {
      const preco = parseFloat(produto.getAttribute('data-preco'));
      const quantidade = parseInt(produto.getAttribute('data-quantidade'));
      total += preco * quantidade;
      total2 += preco * quantidade;
    });
  
    document.getElementById('aVista').textContent = (total + fretePadraoCalc).toFixed(2)
  } else if(cep.value === '') {
      console.log("cep 2 vazio")
  }
})
radioFrete3.addEventListener("click", function() {
  const produtos = document.querySelectorAll('.produto');
  let total = 1;
  let total2 = 1;
  
  if (cep.value.length === 9) {
    freteResumo.textContent = "R$ " + freteExpressCalc.toFixed(2)
    buscarEnderecoPorCep()

    produtos.forEach(produto => {
      const preco = parseFloat(produto.getAttribute('data-preco'));
      const quantidade = parseInt(produto.getAttribute('data-quantidade'));
      total += preco * quantidade;
      total2 += preco * quantidade;
    });
  
    document.getElementById('aVista').textContent = (total + freteExpressCalc).toFixed(2)
  } else if(cep.value === '') {
      console.log("cep 3 vazio")
  }
})

function radioAtivo() {

  if(radioFrete1.checked) {
    freteResumo.textContent = "R$ " + freteEconomicaCalc.toFixed(2)
  } 
  if(radioFrete2.checked) {
    freteResumo.textContent = "R$ " + fretePadraoCalc.toFixed(2)
  }
  if(radioFrete3.checked) {
    freteResumo.textContent = "R$ " + freteExpressCalc.toFixed(2)
  }
}
/* FIM - FRETES RADIOS ATIVOS */

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

/* Botão adicionar e diminuir quantidade */

const quantidadeProd = document.getElementById("quantCarrinhoProd")

function adicionarProduto(idProduto) {
  const produto = document.getElementById(idProduto);
  const preco = parseFloat(produto.getAttribute('data-preco'));
  let quantidade = parseInt(produto.getAttribute('data-quantidade'));

  quantidade += 1;
  produto.setAttribute('data-quantidade', quantidade);
  produto.querySelector('#quantCarrinhoProd').textContent = quantidade;

  const quantidadeProd = document.getElementById(`total-price-${idProduto}`);
  const novoTotal = (preco * quantidade).toFixed(2);
  quantidadeProd.textContent = novoTotal;
  quantidadeProd.setAttribute('data-quantidade', quantidade);

  atualizarTotalCompra();
}

function removerProduto(idProduto) {
  const produto = document.getElementById(idProduto);
  const preco = parseFloat(produto.getAttribute('data-preco'));
  let quantidade = parseInt(produto.getAttribute('data-quantidade'));

  if (quantidade > 1) {
    quantidade -= 1;
    produto.setAttribute('data-quantidade', quantidade);
    produto.querySelector('#quantCarrinhoProd').textContent = quantidade;

    const totalPriceSpan = document.getElementById(`total-price-${idProduto}`);
    const novoTotal = (preco * quantidade).toFixed(2);
    totalPriceSpan.textContent = novoTotal;
    totalPriceSpan.setAttribute('data-quantidade', quantidade);

    atualizarTotalCompra();
  }
}

function atualizarTotalCompra() {
  const produtos = document.querySelectorAll('.produto');
  let total = 1;
  let total2 = 1;

  produtos.forEach(produto => {
    const preco = parseFloat(produto.getAttribute('data-preco'));
    const quantidade = parseInt(produto.getAttribute('data-quantidade'));
    total += preco * quantidade;
    total2 += preco * quantidade;
  });

  valor = `R$ ${total.toFixed(2)}`

  document.getElementById('valorProdutos').textContent = valor;
  total = fretePadraoCalc
  freteResumo.textContent = `R$ ${total.toFixed(2)}`;
  document.getElementById('aVista').textContent = (total2 + total).toFixed(2);
}

/* Desconto */

const descontoButton = document.getElementById("descontoButton")
const cupom = document.getElementById("cupom")
const produtos = document.querySelectorAll('.produto');
const textoPop = document.getElementById('popUpId')
descontoButton.addEventListener('click', cupomDesconto)

let total = 1;

produtos.forEach(produto => {
  const preco = parseFloat(produto.getAttribute('data-preco'));
  const quantidade = parseInt(produto.getAttribute('data-quantidade'));
  total += preco * quantidade;
});

function cupomDesconto() {
  if(cupom.value === "MOTHER10") {
    let por10 = total * 0.10;
    let cupom10 = (total - por10).toFixed(2)

    document.getElementById('aVista').textContent = "R$ "+ cupom10 
    textoPop.textContent = "Cupom de 10% aplicado"
    validar(cupom, cupomInv)
  }else if(cupom.value === "") {
    document.getElementById('aVista').textContent = "R$ "+ total.toFixed(2)
    /* textoPop.textContent = "Sem nenhum cupom aplicado" */
    cupom.classList.remove('is-valid')
    cupom.classList.add('is-invalid')

    setTimeout(() => {
      cupom.classList.remove('is-invalid');
    }, 2000);
  }
}

descontoButton.addEventListener("click", popUp)

function popUp() {
  if(cupom.value === "") {
    descontoButton.setAttribute('data-bs-toggle', "")
  } else if(cupom.value !== "") {
    descontoButton.setAttribute('data-bs-toggle', "modal")
  }
}
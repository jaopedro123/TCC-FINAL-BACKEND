

const items = document.querySelectorAll('.prodcarroseldoprod-item');
const prevButton = document.querySelector('.prodcarroseldoprod-prev');
const nextButton = document.querySelector('.prodcarroseldoprod-next');
let currentItem = 0;
let isTransitioning = false; // Evitar transições simultâneas

function showNextItem() {
    if (isTransitioning) return;
    isTransitioning = true;

    const prevItem = currentItem;
    currentItem = (currentItem + 1) % items.length;

    items[prevItem].classList.remove('active');
    items[prevItem].classList.add('out');

    items[currentItem].classList.remove('out-reverse');
    items[currentItem].classList.add('active');

    setTimeout(() => {
        items[prevItem].classList.remove('out');
        isTransitioning = false;
    }, 500);
}

function showPrevItem() {
    if (isTransitioning) return;
    isTransitioning = true;

    const prevItem = currentItem;
    currentItem = (currentItem - 1 + items.length) % items.length;

    items[prevItem].classList.remove('active');
    items[prevItem].classList.add('out-reverse');

    items[currentItem].classList.remove('out');
    items[currentItem].classList.add('active');

    setTimeout(() => {
        items[prevItem].classList.remove('out-reverse');
        isTransitioning = false;
    }, 500);
}

prevButton.addEventListener('click', showPrevItem);
nextButton.addEventListener('click', showNextItem);

// Funções para o toque
let touchStartX = 0;

function handleTouchStart(event) {
    touchStartX = event.touches[0].clientX;
}

function handleTouchEnd(event) {
    const touchEndX = event.changedTouches[0].clientX;
    const touchDiff = touchStartX - touchEndX;

    if (touchDiff > 50) {
        showNextItem();
    } else if (touchDiff < -50) {
        showPrevItem();
    }
}

// Adiciona os eventos de toque
const container = document.querySelector('.prodcarroseldoprod-container');
container.addEventListener('touchstart', handleTouchStart);
container.addEventListener('touchend', handleTouchEnd);

document.addEventListener("DOMContentLoaded", function() {
    const reviewsContainer = document.getElementById("reviews");
    const reviews = Array.from(reviewsContainer.children);
    const ordenarSelect = document.getElementById("ordenar");
    const filtrarSelect = document.getElementById("filtrar");

    ordenarSelect.addEventListener("change", function() {
        ordenarAvaliacoes(this.value);
    });

    filtrarSelect.addEventListener("change", function() {
        filtrarAvaliacoes(this.value);
    });

    function ordenarAvaliacoes(criterio) {
        const sortedReviews = reviews.slice().sort((a, b) => {
            if (criterio === "recentes") {
                return new Date(b.getAttribute("data-date")) - new Date(a.getAttribute("data-date"));
            } else if (criterio === "antigas") {
                return new Date(a.getAttribute("data-date")) - new Date(b.getAttribute("data-date"));
            } else if (criterio === "melhor_avaliados") {
                return b.getAttribute("data-rating") - a.getAttribute("data-rating");
            } else if (criterio === "pior_avaliados") {
                return a.getAttribute("data-rating") - b.getAttribute("data-rating");
            }
        });

        sortedReviews.forEach(review => reviewsContainer.appendChild(review));
    }

    function filtrarAvaliacoes(estrelas) {
        reviews.forEach(review => {
            if (estrelas === "todas" || review.getAttribute("data-rating") === estrelas) {
                review.style.display = "block";
            } else {
                review.style.display = "none";
            }
        });
    }
});

let precosFrete = {}; // Objeto para armazenar preços de frete por CEP

document.getElementById('cep').addEventListener('input', function(e) {
    let value = e.target.value;
    value = value.replace(/\D/g, ''); // Remove qualquer caractere não numérico
    if (value.length > 5) {
        value = value.replace(/^(\d{5})(\d{0,3}).*/, '$1-$2');
    }
    e.target.value = value;
});
document.getElementById('novoCep').addEventListener('input', function(e) {
    let value = e.target.value;
    value = value.replace(/\D/g, ''); // Remove qualquer caractere não numérico
    if (value.length > 5) {
        value = value.replace(/^(\d{5})(\d{0,3}).*/, '$1-$2');
    }
    e.target.value = value;
});
document.addEventListener('DOMContentLoaded', function () {
    // Máscara para o campo de CEP



    const precosFrete = {};

    function limparAlertas(modal) {
        if (modal) {
            document.getElementById('freteModalBody').innerHTML = '';
        } else {
            document.getElementById('resultado').innerHTML = '';
        }
    }

    function mostrarSpinner(modal) {
        if (modal) {
            document.getElementById('loadingSpinnerModal').style.display = 'block';
        } else {
            document.getElementById('loadingSpinner').style.display = 'block';
        }
    }

    function esconderSpinner(modal) {
        if (modal) {
            document.getElementById('loadingSpinnerModal').style.display = 'none';
        } else {
            document.getElementById('loadingSpinner').style.display = 'none';
        }
    }

    function buscarCep(cep, modal = false) {
        // Limpar alertas anteriores e mostrar o spinner de carregamento
        limparAlertas(modal);
        mostrarSpinner(modal);

        // Verifica se o CEP já está no objeto precosFrete
        if (precosFrete[cep]) {
            // CEP já foi buscado antes, usa o preço armazenado
            esconderSpinner(modal);
            atualizarModal(precosFrete[cep], cep, modal);
        } else {
            // Se o CEP não foi buscado antes, faz a requisição à API
            fetch(`https://viacep.com.br/ws/${cep}/json/`)
                .then(response => response.json())
                .then(data => {
                    esconderSpinner(modal); // Esconder spinner após a resposta da API
                    if (data.erro) {
                        exibirAlerta('CEP não encontrado.', modal);
                    } else {
                        // Gerar preço de frete aleatório
                        var precoFrete = (Math.random() * (50 - 10) + 10).toFixed(2);
                        
                        // Armazenar o preço de frete no objeto
                        precosFrete[cep] = {
                            endereco: `${data.logradouro}, ${data.bairro}, ${data.localidade} - ${data.uf}`,
                            preco: precoFrete
                        };

                        atualizarModal(precosFrete[cep], cep, modal);
                    }
                })
                .catch(() => {
                    esconderSpinner(modal); // Esconder spinner se houver um erro
                    exibirAlerta('Erro ao buscar o CEP. Tente novamente.', modal);
                });
        }
    }

    function exibirAlerta(mensagem, modal) {
        if (modal) {
            document.getElementById('freteModalBody').innerHTML = `<div class="alert alert-danger">${mensagem}</div>`;
        } else {
            document.getElementById('resultado').innerHTML = `<div class="alert alert-danger">${mensagem}</div>`;
        }
    }

    function atualizarModal(dadosFrete, cep, modal) {
        // Atualizar o corpo do modal com o preço do frete
        document.getElementById('freteModalBody').innerHTML = `<h5>Entregas disponiveis para o CEP: <b>${cep}</b> </h5><br/><br/>    <div class="row">
    <div class="col-md-4"><b>Entrega Economica</b></div>
    <div class="col-md-6 ms-auto"><b>R$${dadosFrete.preco}</b>
    Chegará até: 10/11/2024</div>
  </div><br/>`;
        
        if (!modal) {
            // Abrir o modal do Bootstrap
            var freteModal = new bootstrap.Modal(document.getElementById('freteModal'));
            freteModal.show();
        }
    }

    document.getElementById('calcularFreteBtn').addEventListener('click', function () {
        var cep = document.getElementById('cep').value.replace("-", "");
        if (cep.length === 8) {
            buscarCep(cep);
        } else {
            exibirAlerta('<b>Por favor, digite um CEP válido.</b>', false);
        }
    });

    document.getElementById('atualizarFreteBtn').addEventListener('click', function () {
        var novoCep = document.getElementById('novoCep').value.replace("-", "");
        if (novoCep.length === 8) {
            buscarCep(novoCep, true);
        } else {
            exibirAlerta('<b>Por favor, digite um CEP válido.</b>', true);
        }
    });
});
function gerarPrecoAleatorio() {
    return (Math.random() * (50 - 10) + 10).toFixed(2); // Gera um valor aleatório entre R$ 10,00 e R$ 50,00
}
const cartButtons = document.querySelectorAll('.cart-button');

cartButtons.forEach(button => {
    button.addEventListener('click', cartClick);
});

function cartClick() {
    let button = this;
    button.classList.add('clicked');
}
document.getElementById('changeIconBtn').addEventListener('click', function() {
    var icon = this.querySelector('i');
    if (icon.classList.contains('bi-bookmark')) {
        icon.classList.remove('bi-bookmark');
        icon.classList.add('bi-bookmark-fill');
    } else {
        icon.classList.remove('bi-bookmark-fill');
        icon.classList.add('bi-bookmark');
    }
    
    this.classList.add('clicked');
    setTimeout(() => {
        this.classList.remove('clicked');
    }, 300);
});
document.querySelectorAll('.carousel').forEach(carousel => {
    carousel.addEventListener('slid.bs.carousel', function (event) {
      const thumbnails = document.querySelectorAll('.carousel-thumbnails .thumb');
      thumbnails.forEach((thumb, index) => {
        thumb.classList.remove('active');
        if (index === event.to) {
          thumb.classList.add('active');
        }
      });
    });
  });
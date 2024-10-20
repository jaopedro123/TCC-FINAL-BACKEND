$(document).ready(function () {
    const dropdownMarca = $("#marca");
    const dropdownCategorias = $("#categoria");

    $("#buttonCancel").on("click", function () {
        window.location = "/MotherBoardAdmin/produtos";
    });

    $("#descricaoCompleta").richText();
    $("#descricaoCurta").richText();

    dropdownMarca.change(function () {
        dropdownCategorias.empty();
        getCategorias();
    });

    getCategorias();
});

// Função para buscar categorias com base na marca selecionada
function getCategorias() {
    const dropdownMarca = $("#marca");
    const dropdownCategorias = $("#categoria");
    const marcaId = dropdownMarca.val();
    const url = marcamoduleURL + "/" + marcaId + "/categorias";

    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, categoria) {
            $("<option>").val(categoria.id).text(categoria.nome).appendTo(dropdownCategorias);
        });
    });
}

// Função para exibir modal de diálogo
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}

// Permitir apenas números no campo de estoque
$("#estoque").on("input", function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});

$(".number").on("input", function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});

// Função para formatar o valor de entrada como moeda (R$)
function formatarMoeda(element) {
    let value = element.value;

    // Remove qualquer caractere que não seja número ou vírgula
    value = value.replace(/\D/g, "");

    // Adiciona pontos para separar milhar e vírgula para separar centavos
    value = (value / 100).toFixed(2) + "";
    value = value.replace(".", ",");
    value = value.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1.");

    // Define o valor formatado no campo de entrada
    element.value = value;
}

// Aplica o evento de formatação
document.querySelectorAll('.number').forEach(function(input) {
    input.addEventListener('input', function() {
        formatarMoeda(input);
    });
}); 


document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('#ProdutoForm');
    const imagemExtras = document.querySelectorAll('input[name="imagemExtra"]');
    const maxSize = 1 * 1024 * 1024;

    // Adicionar eventos de input e change para validar enquanto o usuário digita
    document.querySelectorAll('.validar').forEach(input => {
        input.addEventListener('input', function () {
            validateInput(input); 
        });

        input.addEventListener('change', function () {
            validateInput(input);
        });
    });

    function validateInput(input) {
        if (input.name === 'nome' || input.name === 'alias') {
            const regex = /^[A-Za-z0-9.\-+=&()_\/]+( [A-Za-z0-9.\-+=&()_\/]+)*$/;


            if (input.value.trim() === '') {
                input.classList.remove('is-valid');
                input.classList.add('is-invalid');
                input.nextElementSibling.textContent = 'Preencha este campo.';
                return false;
            }

            if (input.value.length < 3) {
                input.classList.remove('is-valid');
                input.classList.add('is-invalid');
                input.nextElementSibling.textContent = 'O nome do produto deve ter pelo menos 3 caracteres.';
                return false;
            }

            if (!regex.test(input.value)) {
                input.classList.remove('is-valid');
                input.classList.add('is-invalid');
                // O nome do produto só pode conter letras, números e os seguintes caracteres: . - + = ( ) &. Palavras devem ser separadas por um único espaço.
                input.nextElementSibling.textContent = 'O nome do produto está inválido';
                return false;
            } else {
                input.classList.remove('is-invalid');
                input.classList.add('is-valid');
                input.nextElementSibling.textContent = ''; 
                return true;
            }
        }

        if (input.checkValidity()) {
            input.classList.remove('is-invalid');
            input.classList.add('is-valid');
            if (input.nextElementSibling) {
                input.nextElementSibling.textContent = ''; 
            }
            return true;
        } else {
            input.classList.remove('is-valid');
            input.classList.add('is-invalid');
            if (input.nextElementSibling) {
                input.nextElementSibling.textContent = 'Preencha este campo corretamente.'; 
            }
            return false;
        }
    }

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        event.stopPropagation();

        // Verifica se o formulário é válido com Bootstrap
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        // Validação do campo "Nome da marca" e "Alias"
        const nomeInput = form.querySelector('input[name="nome"]');
        const aliasInput = form.querySelector('input[name="alias"]');
        const isNomeValido = validateInput(nomeInput);
        const isAliasValido = validateInput(aliasInput);

        // Se alguma validação falhar, não continua
        if (!isNomeValido || !isAliasValido) {
            return; 
        }

        let isValid = true; 

        if (fileImage.files.length > 0) {
            if (fileImage.files[0].size > maxSize) {
                showModalDialog("Desculpe...", "Você só pode escolher imagens abaixo de 1MB!");
                addInvalido(fileImage);
                fileImage.nextElementSibling.textContent = 'A imagem deve ter no máximo 1MB.';
                return;
            }
        }

        imagemExtras.forEach(function(imagemExtra) {

            if (imagemExtra.files && imagemExtra.files[0]) {
                const fileSize = imagemExtra.files[0].size;

                if (fileSize > maxSize) {
                    addInvalido(imagemExtra)
                    isValid = false; 
                } else {
                    addCerto(imagemExtra)
                }
            }
        });

        if (!isValid) {
            showModalDialog("Desculpe...", "Você só pode escolher imagens abaixo de 1MB! ");
            return;
        }


        // Executa a validação de unicidade antes de submeter
        checkUniqueProduto(form, nomeInput);
    });
});

// Validação de unicidade antes de submeter o formulário
function checkUniqueProduto(form, nomeInput) {
    const produtoId = $("#id").val();
    const produtoNome = $("input[name='nome']").val();
    const csrfValue = $("input[name='_csrf']").val();
    const params = { id: produtoId, nome: produtoNome, _csrf: csrfValue };

    $.post(checkUniqueUrl, params, function (response) {
        if (response === "OK") {
            form.submit(); // Se a resposta for OK, submete o formulário
        } else if (response === "Duplicado") {
            showModalDialog("Erro ao criar produto", "Tem outro produto com o mesmo nome: " + produtoNome);

            // Aplicar a classe is-invalid ao campo nome e exibir mensagem de erro
            nomeInput.classList.remove('is-valid');
            nomeInput.classList.add('is-invalid');
            nomeInput.nextElementSibling.textContent = 'Nome do produto já em uso. Escolha outro nome.';
        }
    }).fail(function () {
        showModalDialog("Erro", "Erro ao verificar a unicidade do produto. Tente novamente.");
    });
}
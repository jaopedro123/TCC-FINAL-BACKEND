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

    // Validação do formulário
    const form = document.querySelector('.needs-validation');
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        event.stopPropagation();

        // Verifica se o formulário é válido com Bootstrap
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        // Executa a validação de unicidade antes de submeter
        checkUniqueProduto(form);
    });
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

// Validação de unicidade antes de submeter o formulário
function checkUniqueProduto(form) {
    const produtoId = $("#id").val();
    const produtoNome = $("#nome").val();
    const csrfValue = $("input[name='_csrf']").val();
    const params = { id: produtoId, nome: produtoNome, _csrf: csrfValue };

    $.post(checkUniqueUrl, params, function (response) {
        if (response === "OK") {
            form.submit(); // Se a resposta for OK, submete o formulário
        } else if (response === "Duplicado") {
            showModalDialog("Tem outro produto com o mesmo nome: " + produtoNome);
        }
    }).fail(function () {
        showModalDialog("Erro ao verificar a unicidade do produto. Tente novamente.");
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

// Seleciona todos os inputs com a classe 'number' e aplica o evento de formatação
document.querySelectorAll('.number').forEach(function(input) {
    input.addEventListener('input', function() {
        formatarMoeda(input);
    });
}); 
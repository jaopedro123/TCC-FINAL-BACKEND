dropdownMarca = $("#marca");
dropdownCategorias = $("#categoria");

$(document).ready(function () {

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

function getCategorias() {
    marcaId = dropdownMarca.val();
    url = marcamoduleURL + "/" + marcaId + "/categorias"

    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, categoria) {
            $("<option>").val(categoria.id).text(categoria.nome).appendTo(dropdownCategorias);
        });
    });

}

function checkuniqueProduto(form) {
    const produtoId = $("#id").val();
    const produtoNome = $("#nome").val();
    const csrfValue = $("input[name='_csrf']").val();

    const params = { id: produtoId, nome: produtoNome, _csrf: csrfValue };

    $.post(checkUniqueUrl, params, function (response) {

        if (response == "OK") {
            form.submit();
        } else if (response == "Duplicado") {
            showModalDialog("Tem outro produto com o mesmo nome: " + produtoNome);
        }
    })
        .fail(function () {
            showModalDialog("Erro ao verificar a unicidade do produto. Tente novamente.");
        }); return false;
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}
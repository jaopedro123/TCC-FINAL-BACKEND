$(document).ready(function () {

    $("a[name='linkRemoverDetalhes']").each(function(index) {
        $(this).click(function() {
            removerDetalhesSectionByIndex(index);
        })
    })
});

function removerDetalhesSectionById(id) {
    $("#" + id).remove();
}

function removerDetalhesSectionByIndex(index) {
    $("#divDetalhes" + index).remove();
}

function addNextDetalhesSection() {
    allDivDetalhes = $("[id^='divDetalhes']");
    divDetalhesCount = allDivDetalhes.length;

    htmlDetalhesSection = `
        <div class="row my-2" id="divDetalhes${divDetalhesCount}">

             <div class="col">
                <input type="hidden" name="detalhesIDs" value="0" />
                 <label class="my-2 ms-1">Nome:</label>
                 <input type="text" class="form-control" name="detalhesNomes" maxlength="255">
             </div>
             <div class="col">
                 <label class="my-2 ms-1">Valor:</label>
                 <input type="text" class="form-control" name="detalhesValor" maxlength="255">
             </div>

        </div>
    `;

    $("#divProdutoDetalhes").append(htmlDetalhesSection);

    previousDivDetalhesSection = allDivDetalhes.last();
    previousDivDetalhesID = previousDivDetalhesSection.attr("id");

    htmlLinkRemove = `
        <a title="Remover" href="javascript:removeDetalhesSectionById('${previousDivDetalhesID}')">Remover</a>
    `;

    previousDivDetalhesSection.append(htmlLinkRemove);

    $("input[name='detalhesNomes]").last().focus(); 
}

function removeDetalhesSectionById(id) {
    $("#" + id).remove();
}
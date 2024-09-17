function addNextDetalhesSection() {
    allDivDetalhes = $("[id^='divDetalhes']");
    divDetalhesCount = allDivDetalhes.length;

    htmlDetalhesSection = `
        <div class="row my-2" id="divDetalhes${divDetalhesCount}">

             <div class="col">
                 <label class="my-2 ms-1">Nome:</label>
                 <input type="text" class="form-control" name="nomeDetalhes" maxlength="255">
             </div>
             <div class="col">
                 <label class="my-2 ms-1">Valor:</label>
                 <input type="text" class="form-control" name="valorDetalhes" maxlength="255">
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
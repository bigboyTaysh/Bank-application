$(document).ready(function () {
    document.getElementById('currentDate').valueAsDate = new Date();
    document.getElementById('currentDate2').valueAsDate = new Date();

    $("#creditApplication").bind("keyup change", function () {
        var creditAmount = $("input[name=creditAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var creditRates = $("select[name=creditType]").find(':selected').data('rates');

        $("input[name=totalRepayment]").val((f(creditAmount, months, creditRates).valueOf() * months).toFixed(2));
        $("input[name=monthRepayment]").val((f(creditAmount, months, creditRates).valueOf()).toFixed(2));
    });


    if($("select[name=currencyFrom]").find(':selected').text()
        .localeCompare($("select[name=currencyTo]").find(':selected').text()) == 0){
        $("select[name=currencyFrom]").find(':input[type=submit]').prop('disabled', true);
        $("#currencyExchangeMessage").text("Wybierz inną walutę!");
        $("input[name=totalPayment]").val(0.00);
    }

    $("#currencyForm").bind("keyup change", function () {
        //var currencyNameFrom = $("select[name=currencyFrom]").find(':selected').text(); // pobieram name
        //var currencyNameTo = $("select[name=currencyTo]").find(':selected').text() // pobieram name

        if($("select[name=currencyFrom]").find(':selected').text()
            .localeCompare($("select[name=currencyTo]").find(':selected').text()) == 0){
            $(this).find(':input[type=submit]').prop('disabled', true);
            $("#currencyExchangeMessage").text("Wybierz inną walutę!");
            $("input[name=totalPayment]").val(0.00);
        } else {
            //var value = $("input[name=value]").val();
            //var currencyFrom = $("select[name=currencyFrom]").find(':selected').val(); // pobieram name
            //var currencyTo = $("select[name=currencyTo]").find(':selected').val(); // pobieram name
            $(this).find(':input[type=submit]').prop('disabled', false);
            $("#currencyExchangeMessage").text("")

            $("input[name=totalPayment]").val((f2(
                $("input[name=value]").val(),
                $("select[name=currencyFrom]").find(':selected').val(),
                $("select[name=currencyTo]").find(':selected').val()
            ).valueOf()).toFixed(2));
        }


    });

    function f(creditAmount, months, creditRates) {
        let sum = 0.0;
        let commission = 1.0;
        let numberOfInstallmentsPaidDuringTheYear = 12;
        creditAmount = creditAmount * commission;

            for (let i = 1; i <= months; i++){
                sum = sum + Math.pow((1 + ((0.01 * creditRates)/numberOfInstallmentsPaidDuringTheYear)),0-i);
            }
        return creditAmount / sum;
    }

    function f2(value, currencyFrom, currencyTo) {
        return (currencyFrom*value)/currencyTo;
    }

    $("form.search#search-users").submit(function () {
        var pageableSize = document.querySelector("#main > div > div > nav > ul > li.page-item.active > a > span").firstChild.textContent;
        var page = document.querySelector("#main > div > div > nav > ul.pagination.pagination-sm > li.page-item.active > a").textContent;


        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(2)").value = pageableSize;
        document.querySelector("#search-users > div:nth-child(1) > input[type=hidden]:nth-child(1)").value = 0;


    });
});
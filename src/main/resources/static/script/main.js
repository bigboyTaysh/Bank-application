$(document).ready(function () {
    $("#creditAmount").keyup(function () {
        var creditAmount = $("input[name=creditAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var creditRates = $("select[name=creditRates]").val();

        $("input[name=totalRepayment]").val((creditAmount * (1 + (0.01 * creditRates))).toFixed(2));
        $("input[name=monthRepayment]").val((($("input[name=totalRepayment]").val())/months).toFixed(2));
    });

    $("select[name=numberOfMonths]").change(function() {
        var creditAmount = $("input[name=creditAmount]").val();
        var months = $("select[name=numberOfMonths]").val();
        var creditRates = $("select[name=creditRates]").val();

        $("input[name=totalRepayment]").val((creditAmount * (1 + (0.01 * creditRates))).toFixed(2));
        $("input[name=monthRepayment]").val((($("input[name=totalRepayment]").val())/months).toFixed(2));
    });
});
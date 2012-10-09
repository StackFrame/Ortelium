$(function(){

    // Tabs
    $('#tabs').tabs();

    // Dialog
    $('#symbol').dialog({
        autoOpen: false,
        position: "left",
        dialogClass: "absoluteDialog",
        width: 600,
        title: "Symbol",
        buttons: {
            "Ok": function() {
                $(this).dialog("close");
            },
            "Cancel": function() {
                $(this).dialog("close");
            }
        },
        hide: { effect: 'slide', direction: "left" },
        show: { effect: 'slide', direction: "left" }
    });

    $('#query').dialog({
        autoOpen: false,
        position: "right",
        dialogClass: "absoluteDialog",
        width: 600,
        title: "Query",
        buttons: {
            "Ok": function() {
                $(this).dialog("close");
            },
            "Cancel": function() {
                $(this).dialog("close");
            }
        },
        hide: { effect: 'slide', direction: "right" },
        show: { effect: 'slide', direction: "right" }
    });

    // Dialog Link
    $('#symbol_link').click(function(){
        var sidc = $('#sidc_symbol')[0].value;
        var type = $('#radioset input[type=radio]:checked')[0].name;
        var quantityVal = $('#quantity')[0].value;
        var url = "./symbol/2525B/" + sidc + "?outputType=" + type;
        if(quantityVal) {
            var quantity = parseInt(quantityVal,10);
            url += "&C="+quantity;
        }
        
        var img = $("<img />").attr('src', url).load(function() {
                if (!this.complete || typeof this.naturalWidth === "undefined" || this.naturalWidth === 0) {
                    console.log('broken image!');
             } else {
                img.click(function() {
                    $('#sidc_query')[0].value = sidc;
                    $('#query_link').click();
                });
                $('#symbol').html(img);
             }
         });

        $('#symbol').dialog('open');
        return false;
    });

    $('#query_link').click(function(){
        var sidc = $('#sidc_query')[0].value;
        $.ajax({
            url: "./query/2525B/" + sidc,
            success: function(result) {
                var htmlRes = "<table>";
                for(key in result) {
                    var val = result[key];
                    if($.isArray(val)) {
                        htmlRes += "<tr><td colspan='3'>" + key + "</td></tr>";
                        val.forEach(function(item) {
                            if('id' in item) {
                                var itemVal = item.id;
                                htmlRes += "<tr><td>" + itemVal + "</td><td><input class='itemQuery' type='submit' name='" + itemVal + "'></input></td><td> <input class='itemSymbol' name='" + itemVal + "'</input></td></tr>";
                            }
                        });
                    } else {
                        htmlRes += "<tr><td>" + key + "</td><td>" + val + "</td>";
                        
                        if(key === "id" || key === 'parent') {
                           var itemClass = key === 'parent' ? 'itemSymbolQuery' : 'itemSymbol';
                           htmlRes += "<td><input type='submit' class='" + itemClass + "' name='" + val + "'</input></td></tr>"; 
                        } else {
                           htmlRes += "<td></td></tr>";
                        }
                    }
                }
                htmlRes += "</table>";
                $('#query').html(htmlRes);
                $('.itemQuery').button({label: "Query"});
                $('.itemQuery').click(function() {
                    $('#sidc_query')[0].value = this.name;
                    $('#query_link').click();
                });
                
                $('.itemSymbol').button({label: "Symbol"});
                $('.itemSymbol').click(function() {
                    $('#sidc_symbol')[0].value = this.name;
                    $('#symbol_link').click();
                });

                $('.itemSymbolQuery').button({label: "Symbol/Query"});
                $('.itemSymbolQuery').click(function() {
                    var input = this.name.split("?");
                    if(input.length > 0) {
                        var name = input[0];
                        if(input.length > 1) {
                            var quantityArr = input[1].split('=');
                            if(quantityArr.length === 4) {
                                var quantity = parseInt(quantityArr[1],10);
                                $('#quantity')[3].value = quantity;
                            } else {
                                $('#quantity')[0].value = null;
                            }
                        } else {
                            $('#quantity')[0].value = null;
                        }
                        $('#sidc_symbol')[0].value = name;
                        $('#sidc_query')[0].value = name;
                        $('#symbol_link').click();
                        $('#query_link').click();
                    }
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.dir(jqXHR);
            }
        });
        $('#query').dialog('open');
        return false;
    });

    //hover states on the static widgets
    $('#dialog_link, ul#icons li').hover(
        function() { $(this).addClass('ui-state-hover'); },
        function() { $(this).removeClass('ui-state-hover'); }
    );

    $('.itemSymbolQuery').button();
    $('.itemSymbolQuery').click(function() {
        var input = this.name.split("?");
        if(input.length > 0) {
            var name = input[0];
            if(input.length > 1) {
                var quantityArr = input[1].split('=');
                if(quantityArr.length > 1) {
                    var quantity = parseInt(quantityArr[1],10);
                    $('#quantity')[0].value = quantity;
                }
            } else {
                $('#quantity')[0].value = null;
            }
            $('#sidc_symbol')[0].value = name;
            $('#sidc_query')[0].value = name;
            $('#symbol_link').click();
            $('#query_link').click();
        }
    });
});

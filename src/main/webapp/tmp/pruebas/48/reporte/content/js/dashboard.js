/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();
    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter)
        regexp = new RegExp(seriesFilter, 'i');

    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            var newRow = tBody.insertRow(-1);
            for(var col=0; col < item.data.length; col++){
                var cell = newRow.insertCell(-1);
                cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 72.22222222222223, "KoPercent": 27.77777777777778};
    var dataset = [
        {
            "label" : "KO",
            "data" : data.KoPercent,
               "color" : "red"
        },
        {
            "label" : "OK",
            "data" : data.OkPercent,
            "color" : "blue"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round(series.percent)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.7222222222222222, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)  ", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP60model.correoElectronico"], "isController": false}, {"data": [0.0, 500, 1500, "PHCUE2.1-TP4TP60"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP50model.nombre"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP50model.apellidoPaterno"], "isController": false}, {"data": [0.0, 500, 1500, "PHCUE2.1-TP4TP50model.contrasenia"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP60model.nombre"], "isController": false}, {"data": [0.0, 500, 1500, "PHCUE2.1-TP4TP60model.curp"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP60model.apellidoMaterno"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE1-TP10"], "isController": false}, {"data": [0.0, 500, 1500, "PHCUE2.1-TP4TP90model.correoElectronico"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP50model.correoElectronico"], "isController": false}, {"data": [0.0, 500, 1500, "PHCUE2.1-TP4TP60model.contrasenia"], "isController": false}, {"data": [1.0, 500, 1500, "PJCUE2.1-TP60"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP50model.curp"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE13-TP40"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP4TP60model.apellidoPaterno"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2.1-TP10"], "isController": false}, {"data": [1.0, 500, 1500, "PHCUE2-TP10"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 18, 5, 27.77777777777778, 198.8000000000004, 449.0, 449.0, 9.375, 42.67679850260417, 32, 449], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "90th pct", "95th pct", "99th pct", "Throughput", "KB/sec", "Min", "Max"], "items": [{"data": ["PHCUE2.1-TP4TP60model.correoElectronico", 1, 0, 0.0, 52.0, 52.0, 52.0, 19.230769230769234, 94.8016826923077, 52, 52], "isController": false}, {"data": ["PHCUE2.1-TP4TP60", 1, 1, 100.0, 47.0, 47.0, 47.0, 21.27659574468085, 104.7830784574468, 47, 47], "isController": false}, {"data": ["PHCUE2.1-TP4TP50model.nombre", 1, 0, 0.0, 107.0, 107.0, 107.0, 9.345794392523365, 45.68852219626168, 107, 107], "isController": false}, {"data": ["PHCUE2.1-TP4TP50model.apellidoPaterno", 1, 0, 0.0, 88.0, 88.0, 88.0, 11.363636363636363, 55.508700284090914, 88, 88], "isController": false}, {"data": ["PHCUE2.1-TP4TP50model.contrasenia", 1, 1, 100.0, 115.0, 115.0, 115.0, 8.695652173913043, 42.81589673913043, 115, 115], "isController": false}, {"data": ["PHCUE2.1-TP4TP60model.nombre", 1, 0, 0.0, 171.0, 171.0, 171.0, 5.847953216374268, 28.931377923976605, 171, 171], "isController": false}, {"data": ["PHCUE2.1-TP4TP60model.curp", 1, 1, 100.0, 50.0, 50.0, 50.0, 20.0, 98.0859375, 50, 50], "isController": false}, {"data": ["PHCUE2.1-TP4TP60model.apellidoMaterno", 1, 0, 0.0, 60.0, 60.0, 60.0, 16.666666666666668, 82.40559895833334, 60, 60], "isController": false}, {"data": ["PHCUE1-TP10", 1, 0, 0.0, 96.0, 96.0, 96.0, 10.416666666666666, 49.906412760416664, 96, 96], "isController": false}, {"data": ["PHCUE2.1-TP4TP90model.correoElectronico", 1, 1, 100.0, 50.0, 50.0, 50.0, 20.0, 98.3203125, 50, 50], "isController": false}, {"data": ["PHCUE2.1-TP4TP50model.correoElectronico", 1, 0, 0.0, 97.0, 97.0, 97.0, 10.309278350515465, 50.217461340206185, 97, 97], "isController": false}, {"data": ["PHCUE2.1-TP4TP60model.contrasenia", 1, 1, 100.0, 78.0, 78.0, 78.0, 12.82051282051282, 63.38892227564103, 78, 78], "isController": false}, {"data": ["PJCUE2.1-TP60", 1, 0, 0.0, 449.0, 449.0, 449.0, 2.2271714922048997, 0.19139755011135856, 449, 449], "isController": false}, {"data": ["PHCUE2.1-TP4TP50model.curp", 1, 0, 0.0, 78.0, 78.0, 78.0, 12.82051282051282, 62.46243990384615, 78, 78], "isController": false}, {"data": ["PHCUE13-TP40", 1, 0, 0.0, 32.0, 32.0, 32.0, 31.25, 9.6435546875, 32, 32], "isController": false}, {"data": ["PHCUE2.1-TP4TP60model.apellidoPaterno", 1, 0, 0.0, 68.0, 68.0, 68.0, 14.705882352941176, 72.69646139705881, 68, 68], "isController": false}, {"data": ["PHCUE2.1-TP10", 1, 0, 0.0, 59.0, 59.0, 59.0, 16.949152542372882, 79.36639300847457, 59, 59], "isController": false}, {"data": ["PHCUE2-TP10", 1, 0, 0.0, 96.0, 96.0, 96.0, 10.416666666666666, 85.174560546875, 96, 96], "isController": false}]}, function(index, item){
        switch(index){
            case 3:
                item = item.toFixed(2) + '%';
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Test failed: text expected to contain \/El correo no puede contener coma, punto, punto medio, dos puntos o gui\u00C3\u00B3n bajo.\/", 2, 40.0, 11.11111111111111], "isController": false}, {"data": ["Test failed: text expected to contain \/Dato obligatorio.\/", 2, 40.0, 11.11111111111111], "isController": false}, {"data": ["Test failed: text expected to contain \/La Persona ha sido registrada exitosamente.\/", 2, 40.0, 11.11111111111111], "isController": false}, {"data": ["Test failed: text expected to contain \/Escriba menos de 45 caracteres.\/", 3, 60.0, 16.666666666666668], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);
});

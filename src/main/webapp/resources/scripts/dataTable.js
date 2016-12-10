var dataTableCDT = function() {
	
	function editRow(idTable, index, row) {
		$("#" + idTable).DataTable().row(index).data(row);
	}
	
	function deleteRowPasos(table, element) {
		//Se elimina el elemento
		$("#" + table.id).DataTable().row($(element).parents('tr')).remove()
				.draw();
		
		//Se reaordenan la numeración
		var dataTable = $("#" + table.id).dataTable();
		for (var i = 0; i < dataTable.fnSettings().fnRecordsTotal(); i++) {
			dataTable.api().cell(i, 0).data(i + 1);
		}
	}
	
	function moveRow(table, row, direction)
    {
		var dataTable = $("#" + table.id).dataTable()
		
		//Fila seleccionada
		var rowSel = $("#" + table.id).DataTable().row($(row).parents('tr'))
		
		//Se obtiene el índice de la fila que se seleccionó
		var index = $("#" + table.id).DataTable().row($(row).parents('tr')).index();
		
		if(direction == "up") {
			//Se obtiene el índice de la fila anterior
			if(index <= 0) {
				alert("No se puede mover este paso.");
				return false;
			}
			var indexInter = index - 1;
		} else if (direction == "down"){
			//Se obtiene el índice de la fila siguiente
			if(index >= (dataTable.fnSettings().fnRecordsTotal() - 1)) {
				alert("No se puede mover este paso.");
				return false;
			}
			var indexInter = index + 1;
		}
		
		//Se intercambian los números de las filas
		dataTable.api().cell(index, 0).data(indexInter + 1); //Se suma 1 porque los índices comienzan en cero
		dataTable.api().cell(indexInter, 0).data(index + 1); //Se suma 1 porque los índices comienzan en cero
		
		//Se obtienen los valores de las filas completas
		var rowSel = dataTable.api().row(index).data();
		var rowAnt = dataTable.api().row(indexInter).data();
		
		//Se intercambia la información de las columnas
		dataTable.api().row(index).data(rowAnt);
		dataTable.api().row(indexInter).data(rowSel);

		
    }
	/**
	* Permite agregar una fila en una Tabla especificada
	* 
	* @param idTable
	*            {String} identificador de la tabla a la que se quiere agregar
	*            el elemento
	* @param row
	*            {Array} cada uno de los elemento que van a ser agregados en la
	*            fila
	*/
	function addRow(idTable, row) {
		$("#" + idTable).dataTable().fnAddData([ row ], true);
	}

	/**
	* Borra un elemento de la tabla
	* 
	* @param table
	*            {String} tabla a la que se quiere eliminar un elemento
	* 
	* @param row
	*            {Obeject} elemento que van a ser eliminado de la tabla
	*/
	function deleteRow(table, element) {
		$("#" + table.id).DataTable().row($(element).parents('tr')).remove()
				.draw();
	}
	/**
	* Borra todos los elementos de la tabla
	* 
	* @param table
	*            {String} tabla de la que se quieren eliminar todos los
	*            registros
	*/
	function deleteRows(table) {
		$('#' + table).DataTable().rows().remove().draw();
	}
	/**
	* Verifica que un elemento exista en una tabla, en caso de que exista
	* regresa verdadero, verifica un valor de referencia contra los elementos
	* de una columna, en caso de ser requerido mustra un mensaje pasando como
	* parametro el elemento que esta repetido.
	* 
	* @param idTabla
	*            {String} identificador de la tabla en la que se realizará la
	*            comparación
	* @param value
	*            {String} valor de referencia que se busca encontrar en la
	*            tabla
	* @param index
	*            {Integer} índice que indica sobre que columna debe realizarse
	*            la busqueda
	* @param idMessage
	*            {String} (opcional) indica el identificador del texto del
	*            mensaje que se mostrará en caso de que exista un elemento
	*            repetido
	* @param message
	*            {String} mensaje original
	* @returns {Boolean} verdadero en caso de que exista un elemento repetido,
	*          falso en caso contrario
	* 
	*/
	function exist(idTable, value, index, idMessage, message, indexRowExclude) {
		var table = $("#" + idTable).dataTable();
		for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
			if(indexRowExclude != i) {
				if (value === table.fnGetData(i, index)) {
					if (idMessage != undefined && message != undefined
							&& idMessage !== null && idMessage !== '') {
						$("#" + idMessage).text(message.format({
							4 : value
						}));
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	* Crea una tabla por medio del componente DataTables 1.10
	* 
	* @param idTable
	*            {String} identificador del elemento table ha ser creado como
	*            objeto DataTable
	* @param aoColumnDefs
	*            {Object} (opcional) definciones especiales para las columnas
	* @returns Objeto DataTable creado
	*/
	function createDataTable(idTable, aoColumnDefs) {
		var table = $("#" + idTable)
				.DataTable(
						{
							"oLanguage" : {
								"sProcessing" : "Procesando...",
								"sLengthMenu" : "Mostrar _MENU_ registros.",
								"sZeroRecords" : "No se encontraron resultados.",
								"sEmptyTable" : "No se encontraron resultados.",
								"sInfo" : "<label style='font-size: 0.8rem; float: left;'>Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros. </label>",
								"sInfoEmpty" : "<label style='font-size: 0.8rem; float: left;'>Mostrando registros del 0 al 0 de un total de 0 registros. </label>",
								"sInfoFiltered" : "<label style='font-size: 0.8rem; float: left;'>(filtrado de un total de _MAX_ registros).</label>",
								"sInfoPostFix" : "",
								"sSearch" : "",
								"sUrl" : "",
								"sInfoThousands" : ",",
								"sLoadingRecords" : "Cargando...",
								"oPaginate" : {
									"sFirst" : "Primero",
									"sLast" : "Último",
									"sNext" : "<label>Siguiente</label>",
									"sPrevious" : "<label>Anterior</label>"
								},
								"oAria" : {
									"sSortAscending" : ": Activar para ordenar la columna de manera ascendente",
									"sSortDescending" : ": Activar para ordenar la columna de manera descendente"
								}
							},
							"aoColumnDefs" : aoColumnDefs,
							"bJQueryUI" : false,
							"pagingType" : "simple",
							"autoWidth" : false,
						});

		var divFilter = $("#" + idTable).prev();
		var inputSearch = divFilter.find("input");
		inputSearch.attr("placeholder", "Buscar");
		$(".labelSearchDatatable").remove();
		$(".dataTables_filter").append(
				"<label class='labelSearchDatatable'>Buscar:</label>");
		return table;
	}
	
	function insertarValorCelda(idTabla, row, column, data) {
		var tabla = $("#" + idTabla).dataTable();
		if(row == "max") {
			row = tabla.fnSettings().fnRecordsTotal() - 1;
		}
		console.log("row, column: " + row + ", " + column);
		tabla.api().cell(row, column).data(data);
		tabla.api().draw();
	}

	return {
		insertarValorCelda : insertarValorCelda,
		addRow : addRow,
		editRow : editRow,
		deleteRow : deleteRow,
		deleteRows : deleteRows,
		exist : exist,
		createDataTable : createDataTable,
		moveRow : moveRow,
		deleteRowPasos : deleteRowPasos,
	};
}();


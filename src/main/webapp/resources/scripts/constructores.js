/*
 * Constructor del objeto PostPrecondicion
 */
function PostPrecondicion(redaccion, esPrecondicion) {
	this.redaccion = redaccion;
	this.precondicion = esPrecondicion;
}
/*
 * Constructor del objeto Extension
 */
function Extension(idCUDestino, causa, region) {
	this.causa = causa;
	this.region = region;
	this.casoUsoDestino = new CasoUso(idCUDestino);
}

/*
 * Constructor del objeto CasoUso
 */
function CasoUso (id, nombreModulo, numero, nombre) {
	this.id = id;
	this.modulo = new Modulo(nombreModulo);
	this.numero = numero;
	this.nombre = nombre;
}

/*
 * Constructor del objeto Trayectoria
 */
function Trayectoria(clave) {
	this.clave = clave;
}

/*
 * Constructor del objeto Paso
 */
function Paso(numero, realizaActor, verbo, otroVerbo, redaccion, id) {
	this.numero = numero;
	this.realizaActor = realizaActor;
	this.verbo = new Verbo(verbo);
	this.otroVerbo = otroVerbo;
	this.redaccion = redaccion;
	this.id = id;
}

/*
 * Constructor del Verbo
 */
function Verbo(nombre) {
	this.nombre = nombre;
}

/*
 * Constructores del objeto ReglaNegocio
 * */
function ReglaNegocio(numero, nombre, clave, id) {
    this.numero = numero;
    this.nombre = nombre;
    this.clave = clave;
    this.id = id;
}
   
/*
 * Constructor del objeto Actor
 */
function Actor(nombre) {
    this.nombre = nombre;
}

/*
 * Constructor del objeto Atributo
 */
function Atributo(nombre, descripcion, obligatorio, longitud, tipoDato, otroTipoDato, formatoArchivo, tamanioArchivo, unidadTamanio, id) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.obligatorio = obligatorio;
    this.longitud = longitud;
    this.tipoDato = new TipoDato(tipoDato);
    this.formatoArchivo = formatoArchivo;
    this.tamanioArchivo = tamanioArchivo;
    this.unidadTamanio = new UnidadTamanio(unidadTamanio);
    this.otroTipoDato = otroTipoDato; 
    this.id = id;
}

function TipoDato(nombre) {
    this.nombre = nombre;
}

function UnidadTamanio(abreviatura) {
    this.abreviatura = abreviatura;
}

/*
 * Constructor del objeto Mensaje
 */
function Mensaje(numero, nombre, clave, id, redaccion) {
	this.numero = numero;
    this.nombre = nombre;
    this.clave = clave;
    this.id = id;
    this.redaccion = redaccion;
}

/*
 * Constructor del objeto Entidad
 */
function Entidad(nombre, descripcion) {
	this.nombre = nombre;
	this.descripcion = descripcion;
}

/*
 * Constructor del objeto Pantalla
 */
function Pantalla(nombreModulo, numero, nombre, clave, id, patron) {
	this.modulo = new Modulo(nombreModulo);
	this.numero = numero;
	this.nombre = nombre;
	this.clave = clave;
	this.id = id;
	this.patron = patron;
}
function Pantalla(nombreModulo, numero, nombre, clave, id, patron,valoresPantallaTrayectoria) {
	this.modulo = new Modulo(nombreModulo);
	this.numero = numero;
	this.nombre = nombre;
	this.clave = clave;
	this.id = id;
	this.patron = patron;
	this.valoresPantallaTrayectoria = valoresPantallaTrayectoria;
}
function ValorPantallaTrayectoria(id, patron) {
	this.id = id;
	this.patron = patron;
}

/*
 * Constructor del objeto Módulo 
 */
function Modulo(nombre) {
	this.nombre = nombre;
}

/*
 * Constructor del objeto TerminoGlosario
 */
function TerminoGlosario(nombre) {
	this.nombre = nombre;
}

/*
 * Constructor del objeto Parametro
 */
function Parametro(nombre, descripcion, id) {
	this.nombre = nombre;
	this.descripcion = descripcion;
	this.id = id;
}

function Colaborador(curp) {
	this.curp = curp;
}
/*
 * Constructor del objeto Accion
 */
function Accion(nombre, imagen, descripcion, tipoAccion, pantallaDestino, id, url, metodo, pantalla) {
	this.nombre = nombre;
	this.imagen = imagen;
	this.descripcion = descripcion;
	this.tipoAccion = tipoAccion;
	this.pantallaDestino = pantallaDestino;
	this.id = id;
	this.urlDestino = url;
	this.metodo = metodo;
	this.pantalla = pantalla;
}

function TipoAccion(id, nombre) {
	this.id = id;
	this.nombre = nombre;
}

function ImagenAccion(imagen, id) {
	this.imagen = imagen;
	this.id = id;
}

function Entrada(id, etiqueta, valoresEntrada, atributo, terminoGlosario) {
	this.id = id;
	this.nombreHTML = etiqueta;
	this.valores = valoresEntrada;
	this.atributo = atributo;
	this.terminoGlosario = terminoGlosario;
}

function Entrada(id, etiqueta, valoresEntradaTrayectoria, atributo, terminoGlosario) {
	this.id = id;
	this.nombreHTML = etiqueta;
	this.valoresEntradaTrayectoria = valoresEntradaTrayectoria;
	this.atributo = atributo;
	this.terminoGlosario = terminoGlosario;
}

function ValorEntrada(valor, valido, id) {
	this.valor = valor;
	this.valido = valido;
	this.id = id;
}
function ValorEntrada(id, valor,valido,correcto_prueba,correcto_guion) {
	this.id = id;
	this.valor = valor;
	this.valido = valido;
	this.correcto_prueba = correcto_prueba;
	this.correcto_guion = correcto_guion;
}


function ValorEntradaTrayectoria(valor, valido, id) {
	this.valor = valor;
	this.valido = valido;
	this.id = id;
}

function Query(id, query, referenciaParametro) {
	this.id = id;
	this.query = query;
	this.referenciaParametro = referenciaParametro;
}

function ReferenciaParametro(id, queries, valoresMensajeParametro, elementoDestino, paso) {
	this.id = id;
	this.queries = queries;
	this.valoresMensajeParametro = valoresMensajeParametro;
	this.elementoDestino = elementoDestino;
	this.paso = paso;
}

/*function ReferenciaParametro(id, queries, valoresMensajeParametroTrayectoria, elementoDestino, paso) {
	this.id = id;
	this.queries = queries;
	this.valoresMensajeParametroTrayectoria = valoresMensajeParametroTrayectoria;
	this.elementoDestino = elementoDestino;
	this.paso = paso;
}*/




function ValorMensajeParametroTrayectoria(id, valor, idMensajeParametro, idTrayectoria) {
	this.id = id;
	this.valor = valor;
	this.mensajeParametro = new MensajeParametro(idMensajeParametro);
	this.trayectoria = new Trayectoria(idTrayectoria);
}

function ValorMensajeParametro(id, valor, idMensajeParametro) {
	this.id = id;
	this.valor = valor;
	this.mensajeParametro = new MensajeParametro(idMensajeParametro);
}

function MensajeParametro(id, parametro) {
	this.id = id;
	this.parametro = parametro;
}
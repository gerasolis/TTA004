function Regla(numero, nombre) {
    this.numero = numero;
    this.nombre = nombre;
  }
   function Actor(numero, nombre) {
    this.numero = numero;
    this.nombre = nombre;
  }

var reglas = [new Regla("1","Datos correctos"), new Regla("7", "Longitud de datos")]
var actores = [new Actor("1","Responsable"), new Actor("2", "Cartografo")]


  $(function(){
    console.log("desde token.");
    $.fn.atwho.debug = false
    var names = $.map(reglas,function(value,i) {
      return {'id':i, "numero" : value.numero, "nombre" : value.nombre, 'name' : value.numero + " " + value.nombre + ".hola"};
    });

    var at_config = {
      at: "RN.",
      data: names,
      displayTpl: "<li>RN${numero} <small>${nombre}</small></li>",

      limit: 200
    }

    $inputor = $('#inputor').atwho(at_config);
    $inputor.caret('pos', 47);
    $inputor.focus().atwho('run');

    //Actores
    var namesACT = $.map(actores,function(value,i) {
      return {'id':i, "numero" : value.numero, "nombre" : value.nombre, 'name' : value.numero + " " + value.nombre + "."};
    });

    var at_config = {
      at: "ACT.",
      data: namesACT,
      displayTpl: "<li>ACT${numero} <small>${nombre}</small></li>",

      limit: 200
    }

    $inputor = $('#actorInput').atwho(at_config);
    $inputor.caret('pos', 100);
    $inputor.focus().atwho('run');

    
  });

 
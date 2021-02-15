  const messageWindow = document.getElementById("messages");
    const sendButton1 = document.getElementById("send");
    const GameP = document.getElementById("GameId");


    const socket = new WebSocket("ws://localhost:9091/socket");
    var PlayerId = null;
    var GameId = null;

    socket.binaryType = "arraybuffer";


    socket.onopen = function (event) {
        addMessageToWindow("Connected");
        ReJoin();
    };

    socket.onmessage = function (event) {

        // addMessageToWindow(event.data);
         const res = JSON.parse(event.data);

         if(res.Method === "ReJoin"){
         ReJoin();
         }
         else if(res.Method === "UpdatePlayers"){
         UpdatePlayers(event);
         }
         else if(res.Method === "StartGame"){
            window.location = "/Game.html?PlayerId=" + PlayerId +"&GameId=" +GameId;
         }

    };

  function ReJoin(){
      var v = getUrlVars();
      GameId = v["GameId"];
      PlayerId = v["PlayerId"];
      GameP.innerHTML = "Your Game Id is " + GameId;
       var obj = { Method: "ReJoin",PlayerId:PlayerId,GameId:GameId,TellMe:"True"};
        sendMessage(JSON.stringify(obj));
  }

  sendButton1.onclick = function StartGame(){
      var obj = { Method: "StartGame",PlayerId: PlayerId,GameId:GameId}
        sendMessage(JSON.stringify(obj));
  }


   function sendMessage(message) {
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }

  function UpdatePlayers(event){
     messageWindow.innerHTML = "";
    const res = JSON.parse(event.data);

      var obj = JSON.parse(res.State);

       for(var i = 0; i < obj.length; i++){
           addMessageToWindow( "Player " + i + " is " + obj[i]);
       }
  }



   function addMessageToWindow(message) {
        messageWindow.innerHTML += `<li class="list-group-item">${message}</li>`
    }


function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
           addMessageToWindow(value);

    });
       return vars;
}


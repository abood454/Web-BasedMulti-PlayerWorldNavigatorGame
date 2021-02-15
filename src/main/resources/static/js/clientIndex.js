  const messageWindow = document.getElementById("messages");
    const sendButton2 = document.getElementById("Connect");
    const sendButton3 = document.getElementById("create");

    const messageInput = document.getElementById("message");
    const Name = document.getElementById("Name");
    const test = document.getElementById("flexCheckDefault");
    const started = document.getElementById("started");

        const socket = new WebSocket("ws://localhost:9091/socket");

    var PlayerId = null;
    var GameId = null;
    var PlayerStatus = "North";
    var North , East , West , South;


    socket.binaryType = "arraybuffer";

    var obj = { Method: "Connect"};


    socket.onopen = function (event) {
        Connect();
    };

    socket.onmessage = function (event) {

         const res = JSON.parse(event.data);

         if(res.Method === "Connect"){
         ConnetClint(event)
         }
         else if(res.Method === "Create"){
         CreateClint(event)
         window.location = "/Lobby.html?PlayerId=" + PlayerId +"&GameId=" +GameId;

         }
         else if(res.Method === "Join"){
         JoinGame(event)
         window.location = "/Lobby.html?PlayerId=" + PlayerId +"&GameId=" +GameId;

         }
         else if(res.Method === "ReJoin"){
         ReJoin(event);
         }
         else if(res.Method === "ItStarted"){
          started.innerHTML = "The game already started";
         }
    };

      sendButton2.onclick = function (event) {
      if(Name.value != ""){
	      var te;
	     if(test.checked === true)
	      te="Yes"
		  else
		  te = "No"
        var obj = { Method: "Create",PlayerId: PlayerId ,Name:Name.value,Test:te};
        sendMessage(JSON.stringify(obj));
        messageInput.value = "";}
    };

  sendButton3.onclick = function (event) {
        if(Name.value != ""){

         var te;
	     if(test.checked === true)
	      te="Yes"
		  else
		  te = "No"

        var obj = { Method: "JoinGame",PlayerId: PlayerId,GameId:messageInput.value,Name:Name.value,Test:te};
        sendMessage(JSON.stringify(obj));
        messageInput.value = ""}
    };



    function sendMessage(message) {
        socket.send(message);
    }


    function ConnetClint(event){
        const res = JSON.parse(event.data);
         if(PlayerId === null ){
            PlayerId = res.PlayerId;
            localStorage.setItem('Here', PlayerId);
         }
    }

      function CreateClint(event){
        const res = JSON.parse(event.data);
         if(GameId === null ){
            GameId = res.GameId;
         }
    }

      function Connect(){
        var obj = { Method: "Connect"};
        sendMessage(JSON.stringify(obj));
    }

    function JoinGame(event){
       const res = JSON.parse(event.data);
       if(res.State === "Yes"){
       GameId = res.GameId;
       }

    }

    function ReJoin(event){
      const res = JSON.parse(event.data);
      GameId = res.GameId;
      PlayerId = res.PlayerId;
  }



function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });



   return vars;
}


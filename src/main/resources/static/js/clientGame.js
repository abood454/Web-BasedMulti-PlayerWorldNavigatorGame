const messageWindowitems = document.getElementById("items");
	const commands = document.getElementById("commands");
    const sendButton4 = document.getElementById("F");
    const Money = document.getElementById("Money");
    const sendButton5 = document.getElementById("Z");
    const sendButton6 = document.getElementById("S");
    const sendButton7 = document.getElementById("left");
    const Right = document.getElementById("Right");
    const img = document.getElementById("img");

    const Rock = document.getElementById("Rock");
    const Pepper = document.getElementById("Pepper");
    const Scissor = document.getElementById("Scissor");

    const lable = document.getElementById("co");


    const messageInput = document.getElementById("message");




    const socket = new WebSocket("ws://localhost:9091/socket");

    var PlayerId = null;
    var GameId = null;
    var PlayerStatus = "North";
    var North , East , West , South;

    socket.binaryType = "arraybuffer";

    var obj = { Method: "Connect"};


    socket.onopen = function (event) {
        ReJoin();
        Start();
    };
    window.onbeforeunload = function(){
      DropAndLeave();
}
    socket.onclose = function(event) {
       DropAndLeave();
     };

    socket.onmessage = function (event) {

         const res = JSON.parse(event.data);

        if(res.Method === "ReJoin"){
         ReJoin(event);
         }
         else if(res.Method === "Seller"){
         Seller(event);
         }
          else if(res.Method === "Structure"){
         Structure(event);
         co();
         }
         else if(res.Method === "Fight"){
         Fight();
         }
         else if(res.Method === "PlayerItems"){
         PlayerItems(event);
         }
         else if(res.Method === "WINNER!"){
		  addMessageToWindow("WINNER!");

         }
		 else if(res.Method === "Move"){
		 if(res.State === "Yes")
		 addMessageToWindow("You moved");
		 else
		 addMessageToWindow("Closed");
		 }
		 else if(res.Method === "ClosedChest"){
		  if(res.State === "Yes")
		 addMessageToWindow("Closed");
		 else
		 addMessageToWindow("Opened");
		 }
		 else if(res.Method === "YouWinFight"){
          addMessageToWindow("You won the Fight");
         }
          else if(res.Method === "ThereIsFight"){
          addMessageToWindow("You can't move,There is a fight");
         }
         else if(res.Method === "GameOver"){
         		  addMessageToWindow("GameOver!");

         setTimeout(function(){
            window.location.href = '/index.html';
         }, 5000);
         }



    };


  sendButton4.onclick = function (event) {
        var obj = { Method: "Forward",PlayerId: PlayerId,GameId:GameId,facing:PlayerStatus};
        sendMessage(JSON.stringify(obj));
        messageInput.value = "";
    };

    sendButton5.onclick = function (event) {
        var obj = { Method: "Buy",PlayerId: PlayerId,GameId:GameId,facing:PlayerStatus,Key:messageInput.value};
        sendMessage(JSON.stringify(obj));

        messageInput.value = "";
    };


    Right.onclick = function (event) {
          if (PlayerStatus === "North") PlayerStatus = "East";
    else if (PlayerStatus === "East") PlayerStatus = "South";
    else if (PlayerStatus ==="South") PlayerStatus = "West";
    else PlayerStatus = "North";
    co();
    };

     sendButton7.onclick = function (event) {
          if (PlayerStatus === "North") PlayerStatus = "West";
    else if (PlayerStatus === "East") PlayerStatus = "North";
    else if (PlayerStatus ==="South") PlayerStatus = "East";
    else PlayerStatus = "South";
    co();
    };

     Rock.onclick = function (event) {
      var obj = { Method: "RockPepperScissor",PlayerId: PlayerId,GameId:GameId,Move:"Rock"};
        sendMessage(JSON.stringify(obj));
    };
     Pepper.onclick = function (event) {
      var obj = { Method: "RockPepperScissor",PlayerId: PlayerId,GameId:GameId,Move:"Pepper"};
        sendMessage(JSON.stringify(obj));
    };
     Scissor.onclick = function (event) {
      var obj = { Method: "RockPepperScissor",PlayerId: PlayerId,GameId:GameId,Move:"Scissor"};
        sendMessage(JSON.stringify(obj));
    };


    function  co(){
    if(PlayerStatus === "North")
    lable.innerHTML = North;
     else if (PlayerStatus === "East")
    lable.innerHTML = East;
     else if (PlayerStatus ==="South")
    lable.innerHTML = South;
    else
    lable.innerHTML = West;

     if(lable.innerHTML === "Seller")
      img.src = "/images/seller.PNG"
      else if(lable.innerHTML === "Chest")
      img.src = "/images/Chest.PNG"
        else if(lable.innerHTML === "Mirror")
      img.src = "/images/mirorr.PNG"
       else if(lable.innerHTML === "Painting")
      img.src = "/images/painting.PNG"
      else if(lable.innerHTML === "Door")
      img.src = "/images/Door.PNG"
      else if(lable.innerHTML === "NormalWall")
      img.src = "/images/Wall.PNG"

    }



   function sendMessage(message) {
        socket.send(message);
    }

    function ReJoin(){
      var v = getUrlVars();
      GameId = v["GameId"];
      PlayerId = v["PlayerId"];
       var obj = { Method: "ReJoin",PlayerId:PlayerId,GameId:GameId,TellMe:"False"};
        sendMessage(JSON.stringify(obj));
  }

    function Start(){
      var obj = { Method: "Start",PlayerId: PlayerId,GameId:GameId}
       sendMessage(JSON.stringify(obj));
  }




  function Seller(event){
      const res = JSON.parse(event.data);
	    commands.innerHTML = '<ul class="list-group  text-black" id="commands"> </ul>';
       var obj = JSON.parse(res.State);

       for(var i = 0; i < obj.length; i++){
           addMessageToWindow((i+1)+"- Key number " + obj[i]);

       }


  }

   function addMessageToWindow(message) {
    var li =  document.getElementById("commands").getElementsByTagName("li");
        if(!((li[li.length - 1] === "You won the Fight" && message === "You won the Fight")
        || (li[li.length - 1] === "GameOver!" && message === "GameOver!"))){
	   if(li.length >= 4)
	    commands.innerHTML = '<ul class="list-group  text-black" id="commands"> </ul>';
        commands.innerHTML += `<li class="list-group-item">${message}</li>`}
    }


  function addMessageToWindow2(message) {
        messageWindowitems.innerHTML += `<li class="list-group-item">${message}</li>`
    }


  function Structure(event){
      const res = JSON.parse(event.data);
      North = res.North;
      East = res.East;
      West = res.West;
      South = res.South;
  }

  function Fight(){
	      addMessageToWindow("ROCK...PEPPER...SCISSOR");
          addMessageToWindow("Choose one");
  setTimeout(function() {
     var obj = { Method: "EndFight",PlayerId: PlayerId,GameId:GameId};
        sendMessage(JSON.stringify(obj));
    }, 5000)
  }

  function PlayerItems(event){
      const res = JSON.parse(event.data);
       messageWindowitems.innerHTML = '<ul class="list-group  text-black" id="items" ></ul>';
       var obj = JSON.parse(res.State);

       for(var i = 0; i < obj.length; i++){
           if( obj[i] != "-1")
           addMessageToWindow2("Key " + obj[i]);
       }
     Money.innerHTML ="Money = " +  res.Money;

  }

  function DropAndLeave(){
     var obj = { Method: "DropAndLeave",PlayerId: PlayerId,GameId:GameId}
       sendMessage(JSON.stringify(obj));
  }



function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;

    });
       return vars;
}


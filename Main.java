const canvas = document.getElementById("game");
const ctx = canvas.getContext("2d");

const keys = {};

const player = {
    x: 80,
    y: 200,
    width: 32,
    height: 56,
    speed: 5,
    velX: 0,
    velY: 0,
    grounded: false
};

let levelNumber = 1;
let level;



document.addEventListener("keydown", e => {
    keys[e.code] = true;
});

document.addEventListener("keyup", e => {
    keys[e.code] = false;
});



function createLevel(){

    let platforms = [];

    let x = 0;


    // стартовая земля
    platforms.push({
        x:0,
        y:380,
        width:300,
        height:70,
        type:"ground"
    });


    x = 350;


    let lastY = 300;


    let count = 10 + levelNumber * 2;



    for(let i = 0; i < count; i++){


        // изменение высоты
        lastY += Math.floor(Math.random()*100-50);


        if(lastY < 150)
            lastY = 150;

        if(lastY > 330)
            lastY = 330;



        let width =
            Math.floor(
                Math.random()*80
            ) + 100;



        platforms.push({

            x:x,

            y:lastY,

            width:width,

            height:25,

            type:"grass"

        });



        // иногда верхняя секретная платформа

        if(Math.random() > 0.55){

            platforms.push({

                x:x+20,

                y:lastY-100,

                width:100,

                height:20,

                type:"bonus"

            });

        }



        x += width + 
        Math.min(
            80 + levelNumber*3,
            140
        );

    }



    // финишная площадка

    platforms.push({

        x:x,

        y:330,

        width:250,

        height:50,

        type:"ground"

    });



    return {

        platforms:platforms,

        portal:{

            x:x+150,

            y:250,

            width:50,

            height:80

        }

    };

}



function loadLevel(){

    level=createLevel();

    player.x=80;
    player.y=200;

    player.velX=0;
    player.velY=0;

}



loadLevel();





function update(){


    if(keys["KeyA"] || keys["ArrowLeft"])
        player.velX=-player.speed;

    else if(keys["KeyD"] || keys["ArrowRight"])
        player.velX=player.speed;

    else
        player.velX=0;



    if(
        (keys["KeyW"] ||
        keys["Space"] ||
        keys["ArrowUp"])
        &&
        player.grounded
    ){

        player.velY=-14;
        player.grounded=false;

    }



    player.velY+=0.7;


    player.x+=player.velX;
    player.y+=player.velY;



    player.grounded=false;



    for(let p of level.platforms){


        if(

            player.x < p.x+p.width &&
            player.x+player.width > p.x &&
            player.y+player.height > p.y &&
            player.y+player.height < p.y+30 &&
            player.velY>=0

        ){

            player.y=p.y-player.height;
            player.velY=0;
            player.grounded=true;

        }

    }




    let portal=level.portal;


    if(

        player.x < portal.x+portal.width &&
        player.x+player.width > portal.x &&
        player.y < portal.y+portal.height &&
        player.y+player.height > portal.y

    ){

        levelNumber++;
        loadLevel();

    }




    if(player.y>800){

        loadLevel();

    }


}






function drawMario(x, y) {
    const pixel = 4;

    // Каждая строка: [позиция X, позиция Y, ширина, высота, цвет].
    // Координаты заданы в пикселях сетки 4×4.
    const parts = [
        // Шляпа
        [3, 0, 5, 1, "#ff0800"],
        [2, 1, 9, 1, "#ff0800"],

        // Волосы, лицо, ухо и глаз
        [2, 2, 3, 1, "#8d4c16"],
        [5, 2, 2, 2, "#ffad5b"],
        [7, 2, 1, 2, "#000000"],
        [8, 2, 1, 2, "#ffad5b"],
        [1, 3, 1, 2, "#8d4c16"],
        [2, 3, 1, 2, "#ffad5b"],
        [3, 3, 1, 2, "#8d4c16"],
        [4, 3, 3, 2, "#ffad5b"],
        [9, 3, 2, 1, "#ffad5b"],
        [2, 5, 1, 1, "#8d4c16"],
        [3, 5, 5, 1, "#ffad5b"],
        [8, 4, 1, 1, "#000000"],
        [9, 4, 3, 1, "#ffad5b"],
        [7, 5, 4, 1, "#000000"],

        // Рубашка и руки
        [2, 7, 8, 1, "#ff0800"],
        [1, 8, 10, 1, "#ff0800"],
        [0, 9, 12, 1, "#ff0800"],
        [2, 10, 8, 1, "#ff0800"],
        [0, 10, 2, 2, "#ffad5b"],
        [10, 10, 2, 2, "#ffad5b"],

        // Комбинезон и пуговицы
        [4, 7, 1, 3, "#4355b5"],
        [7, 7, 1, 3, "#4355b5"],
        [3, 9, 6, 3, "#4355b5"],
        [2, 11, 8, 2, "#4355b5"],
        [4, 10, 1, 1, "#ffef4f"],
        [7, 10, 1, 1, "#ffef4f"],

        // Ботинки
        [1, 13, 3, 1, "#8d4c16"],
        [0, 14, 4, 2, "#8d4c16"],
        [8, 13, 3, 1, "#8d4c16"],
        [8, 14, 4, 2, "#8d4c16"]
    ];

    for (const [gridX, gridY, width, height, color] of parts) {
        ctx.fillStyle = color;
        ctx.fillRect(
            x + gridX * pixel,
            y + gridY * pixel,
            width * pixel,
            height * pixel
        );
    }
}





function draw(){


    ctx.clearRect(
        0,
        0,
        canvas.width,
        canvas.height
    );


    let camera =
    player.x-250;


    ctx.save();

    ctx.translate(
        -camera,
        0
    );



    // платформы

    for(let p of level.platforms){


        if(p.type==="ground"){

            ctx.fillStyle="#8b4513";

        }
        else if(p.type==="bonus"){

            ctx.fillStyle="#d4b000";

        }
        else{

            ctx.fillStyle="#39a832";

        }


        ctx.fillRect(
            p.x,
            p.y,
            p.width,
            p.height
        );


    }



    // портал

    ctx.fillStyle="#00ff66";

    ctx.fillRect(
        level.portal.x,
        level.portal.y,
        level.portal.width,
        level.portal.height
    );



    drawMario(
        player.x,
        player.y
    );


    ctx.restore();



    ctx.fillStyle="black";
    ctx.font="24px Arial";

    ctx.fillText(
        "Level: "+levelNumber,
        20,
        30
    );

}



function gameLoop(){

    update();
    draw();

    requestAnimationFrame(gameLoop);

}


gameLoop();
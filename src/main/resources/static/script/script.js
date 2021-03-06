const sbmenu = document.getElementById('sbmenu')
const sidebar = document.getElementById('sidebar')
const overlay = document.getElementById('overlay')

let menuOpen = false

function openMenu() {
    menuOpen = true;
    overlay.style.display = 'block'
    sidebar.style.width='340px'
}

function closeMenu() {
    menuOpen = false;
    overlay.style.display = 'none'
    sidebar.style.width='0px'
}

sbmenu.addEventListener('click',function(){
    if(!menuOpen){
        openMenu()
    }
})

overlay.addEventListener('click',function(){
    if(menuOpen){
        closeMenu()
    }
})

/* ------------------------------------------------- */
const searchinput = document.getElementById('searchinput')
const searchb = document.getElementById('searchb')

let isResearching = false

function openResearch(){
    isResearching = true;
    console.log(1);
    searchinput.style.visibility = 'visible'
}

function closeResearch(){
    isResearching = false;
    console.log(2);
    searchinput.style.visibility = 'hidden'
}

searchb.addEventListener('click',function(){
    if(!isResearching){
        openResearch()
    }else{
        closeResearch()
    }
})

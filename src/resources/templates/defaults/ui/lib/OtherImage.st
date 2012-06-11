{if $data.href !=null }{import file="a.st" href=$data.href target=$data.target title=$data.title img=$data.img}
{else}{import file="img.st" img=$data.img}{/if}

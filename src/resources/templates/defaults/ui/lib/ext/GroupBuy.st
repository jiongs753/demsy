<div class="t"></div>
<div class="d">
    <div class="title">{import file="../a.st" href=$data.href target=$data.target title=$data.title name=$data.name}</div>
    <div class="price">团购价:￥{tostring bean=$obj prop="productPrice" pattern="#,#00.00"}</div>
    <div class="img">{import file="../a.st" href=$data.href target=$data.target title=$data.title img=$obj.product.image}</div>
    <a{if $data.href != null} href="{$data.href}"{/if}{if $data.target!=null} target="{$data.target}"{/if}{if $data.title!=null} title="{$data.title}"{/if} class="btn"></a>
<span class="time">&nbsp;{tostring bean=$obj prop="expiredFrom" pattern="MM月dd日"}至{tostring bean=$obj prop="expiredTo" pattern="MM月dd日"}</span>
</div>
<div class="b"></div>

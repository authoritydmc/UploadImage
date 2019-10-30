<?php
//header('Content-type : bitmap; charset=utf-8');
if(isset($_POST["image_str"]))
{
    $img_data=$_POST["image_str"];
    $img_name=$_POST["image_name"];
    $decoded_img=base64_decode($img_data);

    $path='images/'.$img_name;
    $file=fopen($path,"wb");
    $is_written=fwrite($file,$decoded_img);
    fclose($file);
    if($is_written>0)
    {
        #file is written to folder 
        
        $conn = new mysqli("192.168.43.78", "root", "","test");
        
        $query="INSERT INTO images_tbl (name,images_path) values ('$img_name','$path');";
    
        echo $query;

        $result=mysqli_query($conn,$query);
        
        if($result)
        {
echo "success@php";
        }else
        { $row = mysqli_fetch_assoc($result);
            echo $row;
echo "failure@php for sql";
        }
        mysqli_close($conn);
    }else{
        echo "failure#php @ writeFile";
    }

}else
{
    echo "failure to get Image Data";
}
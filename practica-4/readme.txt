Script:

wget https://raw.githubusercontent.com/vacax/virtualhost-proxyreverso/master/basico-ubuntu.sh && chmod +x basico-ubuntu.sh && bash basico-ubuntu.sh


LINK AL VIDEO SOBRE LA PRACTICA:

https://youtu.be/abi9T2m9Ti8


Algunos comandos para la realizacion:

sudo a2ensite    -- Para habilitar el servidor apache.

sudo a2enmod        --Para instalar los modulos faltantes.

sudo a2enmod proxy proxy_html proxy_http       ---Comando para las dependencias de los modulos.

sudo netstat -putona | grep java              --- Permite visualizar que procesos y puertos estan arriba ejecutandose con java.

sudo certbot certonly -m sjfl0001@ce.pucmm.edu.do -d 10143611-a.friasluna.me     ----Para crear el certificado.

sudo service apache2 stop       --Para detener servicio de Apache en el puerto 80.

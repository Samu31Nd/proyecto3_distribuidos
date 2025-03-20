# Proyecto 3
Sistemas distribuidos.

**Instrucciones**
Elabore una aplicación en Java que acepte desde la línea de comandos el numero n de asteroides aleatorios a generarse. Al ejecutarse deberá crearse un hilo por cada asteroide donde cada asteroide tendrá un tamaño aleatorio (que no supere un octavo del área de la pantalla), y con un numero de lados aleatorio. Un hilo se encargará exclusivamente de dibujar los asteroides en la pantalla y todos los asteroides comenzarán en posiciones aleatorias. Cada hilo se encargará de calcular la siguiente posición a donde se debe redibujar su asteroide, y esta información deberá ser compartida al hilo que dibuja los asteroides. Los asteroides se deben desplazar en un movimiento rectilíneo uniforme bajo la consideración de que su velocidad tiene que ser inversamente proporcional a su área.

En el caso de que dos asteroides colisionen ambos deberán reducir su tamaño un 50% y seguirán desplazándose en la misma dirección, pero su velocidad cambiará debido a la reducción de su área.

**NOTA:**
Añadir el argumento N de número de asteroides.
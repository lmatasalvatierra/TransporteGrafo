/**
 * Esta clase representa a un Grafo NO Dirigido con aristas ponderadas (con valores double) implementado con una
 * matriz de adyacencia. Este tipo de Grafo se crea con un conjunto de vertices concreto (identificados del 0..V-1)
 * no dinamico (una vez creado el objeto pueden incluirse y eliminarse aristas, pero el numero de vertices es estatico)
 */

import java.util.ArrayList;

public class GrafoMA_ND extends Grafo {

    private static final double YA_VISITADO = Double.MAX_VALUE;
    private static final int SIN_PREVIO=-1;
    protected double[][] grafo;


    public GrafoMA_ND(int numVertices) {
        super(numVertices, 0);
        grafo = new double[numVertices][numVertices];
        for (int v = 0; v < numVertices; v++)
            for (int vAd = 0; vAd < numVertices; vAd++)
                grafo[v][vAd] = SIN_ARISTA;
    }

    /**
     * Comprueba si una arista concreta se encuentra en el grafo
     * @param arista Arista a buscar en el grafo (vertices origen y destino)
     * @return true si en el grafo existe la arista que une los vertice origen y destino. False en caso contrario
     */
    public boolean existeArista(Arista arista) {
        return grafo[arista.getOrigen()][arista.getDestino()] != SIN_ARISTA;
    }

    /**
     * Devuelve el peso de la arista
     * @param arista Arista cuyo peso se desea buscar en el grafo (vertices origen y destino)
     * @return peso de la arista, en caso de que exista, o Double.POSITIVE_INFINITY en caso de que la arista no exista
     */
    public double getPesoArista(Arista arista) {
        return grafo[arista.getOrigen()][arista.getDestino()];
    }

    /**
     * Inserta la arista (vertices origen y destino) en el grafo (si no existe). En caso de que la arista ya exista
     * este metodo no tiene ningun efecto en el grafo. En caso de insertarse la arista, el peso asignado es 1.
     * @param arista Arista a insertar en el grafo (vertices origen y destino)
     */
    public void insertaArista(Arista arista) {
        insertaArista(arista, 1);
    }

    /**
     * Inserta la arista (vertices origen y destino) en el grafo (si no existe) con el peso indicado. Al tratarse de un grafo
     * no dirigido, se añaden las aristas (origen, destino) y (destino, origen), aunque sólo cuentan como 1 arista. En caso
     * de que la arista ya exista este metodo no tiene ningun efecto en el grafo.
     * @param arista Arista a insertar en el grafo (vertices origen y destino)
     * @param peso   peso a asignar a la arista
     */

    public void insertaArista(Arista arista, double peso) {
        if (!existeArista(arista)) {
            grafo[arista.getOrigen()][arista.getDestino()] = peso;
            grafo[arista.getDestino()][arista.getOrigen()] = peso;
            incNumAristas();

        }
    }

    /**
     * Devuelve el vector que indica qué vertices son adyacentes al vertice origen
     * @param origen vertice origen
     * @return vetor de tamaño V qué indica qué vertices son adyacentes a origen y con qué peso y cuales
     * no (Double.POSITIVE_INFINITY)
     */
    public double[] getAdyacentesDe(int origen) {
        return grafo[origen];
    }


    /**
     * ESTE METODO DEBERA IMPLEMENTARSE OBLIGATORIAMENTE PARA QUE LA PRACTICA SEA ACEPTADA
     * Este metodo debera devolver el camino mas corto entre los vertices origen y destino, asi como la distancia de este camino
     * @param origen vertice origen (valor entre 0..numVertices-1)
     * @param destino vertice destino (valor entre 0..numVertices-1)
     * @param distancia objeto de tipo Real donde se debera devolver la longitud del camino mas corto obtenido
     * @return ArrayList de identificadores de los vertices del grafo que recorre el camino mas corto obtenido. Este ArrayList debera
     * incluir como primer elemento el identificador origen y, como ultimo elemento el identificador destino
     */
    public ArrayList<Integer> caminoMasCorto(int origen, int destino, Real distancia){
        ArrayList<Integer> camino = new ArrayList<Integer>();
        double[] menorCoste = new double[numVertices];
        int [] verticeMasCercano = new int[numVertices];
        dijkstra(origen,menorCoste,verticeMasCercano);
        distancia.setValor(menorCoste[destino]);
        generarCamino(origen, destino, verticeMasCercano, camino);
        return camino;
    }

    private void generarCamino(int origen, int destino, int[] camino, ArrayList<Integer> verticesCamino){
        if(origen != camino[destino]){
            generarCamino(origen, camino[destino], camino, verticesCamino);
            verticesCamino.add(destino);
        }
        else{
            verticesCamino.add(origen);
            verticesCamino.add(destino);
        }
    }
    public void dijkstra(int origen, double[] menorCoste, int [] verticeMasCercano){
        boolean [] visitados= new boolean[numVertices];
        int vertice;

        inicializarDijkstra(menorCoste, visitados,verticeMasCercano,origen);
        for(int i=1; i<numVertices; i++){
            vertice=seleccionarVertice(menorCoste, visitados);
            visitados[vertice]=true;
            for (int j=0; j<numVertices; j++){
                if(!visitados[j]){
                    if (menorCoste[j]> menorCoste[vertice] + grafo[vertice][j]){
                        menorCoste[j]= menorCoste[vertice] + grafo[vertice][j];
                        verticeMasCercano[j]= vertice;
                    }
                }
            }
        }
    }

    private void inicializarDijkstra(double [] menorCoste, boolean[] visitados, int[] verticeMasCercano, int origen){
        for (int i=0; i< numVertices; i++){
            visitados[i]=false;
            menorCoste[i]=grafo[origen][i];
            if(grafo[origen][i]!= SIN_ARISTA)
                verticeMasCercano[i]=origen;
            else verticeMasCercano[i] = SIN_PREVIO;
        }
        visitados[origen]= true;
    }

    private int seleccionarVertice(double[] menorCoste, boolean[]visitados){
        int vertice=0;
        double menor;
        menor = Integer.MAX_VALUE;
        for (int i=0; i<numVertices; i++){
            if(!visitados[i] && (menorCoste[i]<menor)){
                menor= menorCoste[i];
                vertice=i;
            }
        }
        return vertice;
    }
    /**
     * ESTE METODO DEBERA IMPLEMENTARSE OBLIGATORIAMENTE PARA QUE LA PRACTICA SEA ACEPTADA
     * Obtiene los puentes del grafo (aristas cuya eliminacion haria que el grafo quedase fragmentado en mas de una componente conexa)
     * @return ArrayList de aristas identificadas como puentes
     */
    public ArrayList<Arista> getPuentes(){
        ArrayList<Arista> puentes = new ArrayList<Arista>();
        ArrayList<Arista> aristas = listaDeAristas();
        boolean [] visitados= new boolean[numVertices];
        double peso;
        Arista arista;
        while(!aristas.isEmpty()){
            arista = aristas.remove(0);
            peso= grafo[arista.getOrigen()][arista.getDestino()];
            agregarOEliminarArista(arista, false, peso);
            inicializarVisitados(visitados);
            recorridoProfundidad(0, visitados);
            if(!todosVisitados(visitados)){
                puentes.add(new Arista(arista.getOrigen(), arista.getDestino()));
            }
            agregarOEliminarArista(arista, true, peso);

        }
        return puentes;
    }

    private void agregarOEliminarArista(Arista arista, boolean sentido, double peso){
        if(!sentido){
            grafo[arista.getOrigen()][arista.getDestino()]= SIN_ARISTA;
            grafo[arista.getDestino()][arista.getOrigen()]=SIN_ARISTA;
        }
        else {
            grafo[arista.getOrigen()][arista.getDestino()]= peso;
            grafo[arista.getDestino()][arista.getOrigen()]= peso;
        }

    }

    private  ArrayList<Arista> listaDeAristas(){
        ArrayList<Arista> aristas= new ArrayList<Arista>();
        for(int i=0; i< numVertices; i++){
            for (int j=i; j<numVertices; j++){
                if (grafo[i][j]!=SIN_ARISTA){
                    aristas.add(new Arista(i,j));
                }
            }
        }
        return aristas;
    }


    private void inicializarVisitados(boolean [] visitados){
        for (int i=0; i<numVertices; i++){
            visitados[i]=false;
        }
    }

    private void recorridoProfundidad(int origen, boolean [] visitados){
        visitados[origen] = true;
        for (int adyacente=0; adyacente<numVertices; adyacente++){
            if(grafo[origen][adyacente]!=SIN_ARISTA){
                if(!visitados[adyacente])
                    recorridoProfundidad(adyacente, visitados);
            }
        }
    }

    private boolean todosVisitados(boolean [] visitados){
        boolean todosVisitados = true;
        int i=0;
        while (i<visitados.length && todosVisitados){
            if(!visitados[i]){
                todosVisitados=false;
            }
            i++;
        }
        return todosVisitados;
    }

    /**
     * LA IMPLEMENTACION DE ESTE METODO ES OPCIONAL PARA PODER ENTREGAR LA PRACTICA (2 PUNTOS)
     * Obtiene las articulaciones del grafo (vertices cuya eliminacion haria que el grafo quedase fragmentado en mas de una componente conexa)
     * @return ArrayList de identificadores de los vertices del grafo de tipo articulacion
     */
    public ArrayList<Integer> getArticulaciones(){
        ArrayList<Integer> articulaciones = new ArrayList<Integer>();
        double grafoAuxiliar[][]= new double[numVertices][numVertices];
        boolean [] visitados= new boolean[numVertices];
        int i=0;

        while (i< numVertices) {
            inicializarVisitados(visitados);
            visitados[i]=true;
            if(i!=0)
                recorridoProfundidad(0, visitados);
            else recorridoProfundidad(1, visitados);
            if (!todosVisitados(visitados, i)) {
                articulaciones.add(i);
            }
            i++;
        }
        return articulaciones;
    }


    private boolean todosVisitados(boolean [] visitados, int vertice){
        boolean todosVisitados = true;
        int i=0;
        while (i<visitados.length && todosVisitados){
            if (!visitados[i] && i!=vertice) {
                todosVisitados = false;
            }
            i++;
        }
        return todosVisitados;
    }
    /**
     * LA IMPLEMENTACION DE ESTE METODO ES OPCIONAL PARA PODER ENTREGAR LA PRACTICA (2 PUNTOS)
     * Obtiene los centros del grafo (vertices que tienen la menor de las excentricidades del grafo, siendo la excentricidad de un vertice la maxima
     * de las distancias entre cualquiera de los vertices del grafo y el vertice en cuestion)
     * @return ArrayList de identificadores de los vertices del grafo de tipo centro
     */
    public ArrayList<Integer> getCentros(){
        ArrayList<Integer> centros = new ArrayList<Integer>();
        double[][] caminosMinimos= new double[numVertices][numVertices];
        int[][] verticeMasCercano= new int[numVertices][numVertices];
        double [] excentricidad= new double[numVertices];
        double excentricidadMinima= Double.MAX_VALUE;
        floyd(caminosMinimos,verticeMasCercano);


        for (int i=0; i<numVertices; i++){
            excentricidad[i] = getExcentricidad(caminosMinimos[i]);
        }
        for (int j=0; j<numVertices;j++) {
            if (excentricidadMinima > excentricidad[j])
                excentricidadMinima = excentricidad[j];
        }
        for (int i=0; i<numVertices; i++){
            if(excentricidad[i]==excentricidadMinima)
                centros.add(i);
        }

        return centros;
    }

    private double getExcentricidad(double []caminosMinimos){
        double excentricidad= caminosMinimos[0];
        for (int i=1; i<numVertices; i++){
            if(excentricidad<caminosMinimos[i])
                excentricidad=caminosMinimos[i];
        }
        return excentricidad;
    }

    private void inicializarFloyd(double [][] caminosMinimos, int[][] verticeMasCercano){
        for (int i=0; i< numVertices; i++){
            for (int j=0; j<numVertices; j++){
                caminosMinimos[i][j] = grafo [i][j];
                if (grafo[i][j] != SIN_ARISTA)
                    verticeMasCercano[i][j]= i;
                else verticeMasCercano[i][j]=SIN_PREVIO;
            }
        }
        for (int i= 0; i<numVertices; i++){
            caminosMinimos[i][i]=0;
            verticeMasCercano[i][i]= SIN_PREVIO;
        }
    }

    private void floyd(double[][] caminosMinimos, int [][] verticeMasCercano){
        inicializarFloyd(caminosMinimos, verticeMasCercano);
        for (int k=0; k< numVertices; k++){
            for (int i=0; i<numVertices; i++){
                if((i!=k) && (caminosMinimos[i][k] != SIN_ARISTA)){
                    for (int j=0; j<numVertices; j++){
                        if((k!=j) && (caminosMinimos[k][j] !=SIN_ARISTA) && (caminosMinimos[i][k] + caminosMinimos[k][j] < caminosMinimos[i][j])){
                            caminosMinimos[i][j] = caminosMinimos[i][k] + caminosMinimos[k][j];
                            verticeMasCercano[i][j]=k;
                        }
                    }
                }
            }
        }

    }

}
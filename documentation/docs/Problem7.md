# Problem 7 - "Es gibt viel zu tun! Wer macht's"

Während die Bürger der Stadt Schilda ganz begeistert von Ihnen sind, bekommen Sie immer mehr Aufträge, die Sie gar nicht mehr alleine bewältigen können. Sie stellen also neues Personal für die Projektleitung ein. Jeder Mitarbeiter hat unterschiedliche Kompetenzen und Sie wollen die Mitarbeiter so auf die Projekte verteilen, dass jedes Projekt von genau einem Mitarbeiter oder einer Mitarbeiterin mit den notwendigen Kompetenzen geleitet wird. 

Wie ordnen Sie die Mitarbeiter den Projekten zu? (Genau ein Mitarbeiter pro Projekt) (Auch diesen Algorithmus integrieren Sie
in Ihr Tool – schließlich möchte auch die Graphschaft ihre Kräfte gut einsetzen!)

## Modellierung des Problems

Das Problem lässt sich als Graphenmodell mit gerichteten Kanten darstellen. Die Mitarbeiter und Kompetenzen werden als Knoten dargestellt. Die Fähigkeit eines Mitarbeiters eine bestimmte Kompetenz zu besitzen wird als Kante dargestellt. Die Kanten werden mit der Kapazität 1 versehen. 

Um den Graph zu modellieren werden die Java-Bibliotheken `JGraphT` und `JGraphX` verwendet. Mit `JGraphT` wird der Graph als Datenstruktur modelliert. Mit `JGraphX` wird der Graph als Grafik dargestellt und auf dem Bildschirm dargestellt.

## Die Eingabe

Die Buchstaben für die Mitarbeiter werden mit A - G bezeichnet und die Kompetenzen mit T - Z. Hierbei ist es wichtig anzumerken, dass die Anzahl der Mitarbeiter gleich der Anzahl der Kompetenzen sein muss und Jede Kompetenz von mindestens einem Mitarbeiter besetzt sein muss.

```
Person:             Kompetenz:
A: Frau Maier       T: Straßenbau
B: Frau Müller      U: Verkehrsplanung
C: Frau Augst       V: Archäologie
D: Frau Schmidt     W: Gesamtkoordination
E: Herr Kunze       X: Festplanung
F: Herr Hof         Y: Wasserversorgung
G: Frau Lustig      Z: Wettkampfausrichtung
```

Die Eingabe besteht aus einem Graphen, der aus Kanten und Knoten besteht. Diese werden aus einer `.txt` Datei gelesen und in eine Instanz der Klasse `AdjazenzMatrix.java` geladen. Diese Instanz dient als Basis für die Berechnung des minimalen Spannbaums.

```js
// code/data/problem7.txt
  A B C D E F G T U V W X Y Z
A 0 0 0 0 0 0 0 1 1 1 0 0 0 0
B 0 0 0 0 0 0 0 0 0 0 1 1 0 0
C 0 0 0 0 0 0 0 1 0 0 0 0 1 0
D 0 0 0 0 0 0 0 1 1 0 0 0 0 1
E 0 0 0 0 0 0 0 0 0 1 0 1 0 0
F 0 0 0 0 0 0 0 1 0 0 1 0 0 0
G 0 0 0 0 0 0 0 0 0 0 0 1 0 1
T 0 0 0 0 0 0 0 0 0 0 0 0 0 0
U 0 0 0 0 0 0 0 0 0 0 0 0 0 0
V 0 0 0 0 0 0 0 0 0 0 0 0 0 0
W 0 0 0 0 0 0 0 0 0 0 0 0 0 0
X 0 0 0 0 0 0 0 0 0 0 0 0 0 0
Y 0 0 0 0 0 0 0 0 0 0 0 0 0 0
Z 0 0 0 0 0 0 0 0 0 0 0 0 0 0
```

## Die Ausgabe

Die Ausgabe wird als Graph in einem Fenster dargestellt und in die Datei `7 Aufgabenplaner.txt` geschrieben. Das Fenster besteht aus drei Teilen. Im oberen linken Teil wird der Eingabe-Graph mit zusätzlichen Start- und Endknoten dargestellt. Im oberen rechten Teil wird der Ausgabe-Graph mit Start- und Endknoten dargestellt. Im unteren Teil wird die resultierende Aufgabenverteilung ausgegeben.

Vor der Ausgabe werden die inversen/negativen Kanten, Startknoten, Endknoten und deren Kanten entfernt. Diese werden nicht benötigt, da sie keine Rolle spielen. 

Ein korrekte Ausgabe erfüllt folgende Eigenschaften:

- Es müssen alle Kompetenzen und Mitarbeiter in der Ausgabe vorkommen.

- Jeder Mitarbeiter muss genau eine Kompetenz besitzen.

- Es darf keine Kante geben, die von einem Mitarbeiter zu einem anderen Mitarbeiter führt.

- Es darf keine Kante geben, die von einer Kompetenz zu einer anderen Kompetenz führt.

- Es darf keine Kante geben, die von einer Kompetenz zu einem Mitarbeiter führt.

- Alle Kanten müssen gerichtet und ungewichtet sein, also müssen den Fluss 1 besitzen

![Problem7](images/problem7.png)

```js
// code/output/7 Aufgabenplaner.txt
  A B C D E F G T U V W X Y Z 
A 0 0 0 0 0 0 0 0 1 0 0 0 0 0 
B 0 0 0 0 0 0 0 0 0 0 1 0 0 0 
C 0 0 0 0 0 0 0 0 0 0 0 0 1 0 
D 0 0 0 0 0 0 0 0 0 0 0 0 0 1 
E 0 0 0 0 0 0 0 0 0 1 0 0 0 0 
F 0 0 0 0 0 0 0 1 0 0 0 0 0 0 
G 0 0 0 0 0 0 0 0 0 0 0 1 0 0 
T 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
U 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
V 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
W 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
X 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
Y 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
Z 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
```

### Resultierende Aufgabenverteilung

```txt
A: Frau Maier     ->   U: Verkehrsplanung
B: Frau Müller    ->   W: Gesamtkoordination
C: Frau Augst     ->   Y: Wasserversorgung
D: Frau Schmidt   ->   Z: Wettkampfausrichtung
E: Herr Kunze     ->   V: Archäologie
F: Herr Hof       ->   T: Straßenbau
G: Frau Lustig    ->   X: Festplanung
```

## Geeignete Algorithmen

Es gibt verschiedene Algorithmen, die verwendet werden können, um den maximalen Fluss in einem gerichteten Graph zu berechnen. Einige dieser Algorithmen sind:

**Ford-Fulkerson-Algorithmus**: Dieser Algorithmus ist ein iterativer Algorithmus, der in jedem Schritt den Fluss durch einen Pfad erhöht, der vom Quellknoten zum Zielknoten führt und dessen Kapazität noch nicht vollständig ausgeschöpft ist. Der Algorithmus endet, wenn kein solcher Pfad mehr existiert.

**Dinic-Algorithmus**: Dieser Algorithmus ist ebenfalls ein iterativer Algorithmus, der den Fluss durch den Graph in jedem Schritt erhöht, indem er einen Pfad vom Quellknoten zum Zielknoten sucht, dessen Kapazität noch nicht vollständig ausgeschöpft ist. Im Gegensatz zum Ford-Fulkerson-Algorithmus verwendet der Dinic-Algorithmus jedoch eine Heuristik, um schneller zum Ergebnis zu gelangen.

**Edmonds-Karp-Algorithmus**: Dieser Algorithmus ist eine Variation des Ford-Fulkerson-Algorithmus und verwendet auch eine Heuristik, um schneller zum Ergebnis zu gelangen. Im Gegensatz zum Dinic-Algorithmus verwendet der Edmonds-Karp-Algorithmus jedoch eine Breitensuche statt einer Tiefensuche, um Pfade im Graph zu finden.

**Preflow-Push-Algorithmus**: Dieser Algorithmus ist ein schneller, parallelisierbarer Algorithmus, der den Fluss durch den Graph in jedem Schritt erhöht, indem er einen Pfad vom Quellknoten zum Zielknoten sucht, dessen Kapazität noch nicht vollständig ausgeschöpft ist. Im Gegensatz zu den anderen Algorithmen, die hier aufgeführt sind, ist der Preflow-Push-Algorithmus jedoch nicht iterativ, sondern arbeitet in einem einzelnen Durchgang.

## Die Laufzeit des Algorithmus

Zuerst wird aus der Eingabematrix eine neue Matrix erstellt, die einen zusätzlichen Start- und Endknoten enthält. Für jeden Knoten aus der ersten Hälfte der Knoten wird eine Kante vom Startknoten zu diesem Knoten hinzugefügt. 
Das gleiche wird umgekehrt für die zweite Hälfte der Knoten mit dem Endknoten gemacht. Die Laufzeit hierfür beträgt O(V^2).	

Die Laufzeit der Funktion `bfs()` ist O(V + E). In jedem Schritt wird ein Knoten aus der Warteschlange entfernt und die Nachbarknoten des Knotens werden in die Warteschlange aufgenommen. Da jeder Knoten nur einmal in die Warteschlange aufgenommen wird und jede Kante nur einmal betrachtet wird, beträgt die Laufzeit O(V + E).

Die Laufzeit des Ford-Fulkerson-Algorithmus ist O(V * E^2). Der Algorithmus wird in jedem Schritt iterativ ausgeführt, bis kein Pfad mehr vom Quellknoten zum Zielknoten verfügbar ist, der dessen Kapazität noch nicht vollständig ausgeschöpft hat. In jedem Schritt wird eine Breitensuche ausgeführt, um einen solchen Pfad zu finden. 

Da am Ende der `fordFulkerson(int[][] matrix)` Funktion noch eine Ausgabematrix erzeugt wird erhöht sich die Laufzeit um O(V^2). Mit der gleichen Laufzeit werden zusätzlich noch die inversen Kanten des Graphen entfernt.

```java
// Filtere die inversen Kanten
for (int i = 0; i < matrix_output.length; i++)
  for (int j = 0; j < matrix_output[i].length; j++)
    if (matrix_output[i][j] < 0)
      matrix_output[i][j] = 0;
```

Nachdem die inversen Kanten entfernt wurden, wird eine neue Ausgabematrix ohne Start- und Endknoten und ohne deren Kanten erstellt. Die Laufzeit hierfür beträgt O(V^2).

```java
// Erstelle eine neue Ausgabe-Adjazenzmatrix ohne Start- und Endknoten und ohne
// die Kanten zu diesen Knoten
int[][] matrix_output2 = new int[matrix_output.length - 2][matrix_output[0].length - 2];
char[] vertex_letters_new2 = new char[vertex_letters_new.length - 2];

for (int i = 0; i < matrix_output2.length; i++) {
  for (int j = 0; j < matrix_output2[i].length; j++)
    matrix_output2[i][j] = matrix_output[i + 1][j + 1];
  vertex_letters_new2[i] = vertex_letters_new[i + 1];
}
```

Daraus folgt eine Laufzeit von O(V * E^2 + V^2).

## Die Implementierung des Algorithmus

Zur Lösung des Problems wurde der Ford-Fulkerson-Algorithmus verwendet. Genauer gesagt wurde der Edmonds-Karp-Algorithmus verwendet, da dieser eine Breitensuche verwendet, um Pfade im Graph zu finden.  

Zuerst wird aus der Eingabematrix eine neue Matrix erstellt, die einen zusätzlichen Start- und Endknoten enthält. Für jeden Knoten aus der ersten Hälfte der Knoten wird eine Kante vom Startknoten zu diesem Knoten hinzugefügt. 
Das gleiche wird umgekehrt für die zweite Hälfte der Knoten mit dem Endknoten gemacht.

Danach wird die Matrix `matrix` in eine echte Kopie `output` kopiert. Die echte Kopie wird später als Ausgabe verwendet.

Danach wird ein Eltern-Array `parent` erstellt, das die Elternknoten der Knoten im Graph speichert. Dieses Array wird später verwendet, um den Pfad vom Quellknoten zum Zielknoten zu finden.

Als nächstes wird eine Breitensuche ausgeführt, um einen Pfad vom Quellknoten zum Zielknoten zu finden, dessen Kapazität noch nicht vollständig ausgeschöpft ist. Die Breitensuche wird durch die Funktion `bfs()` ausgeführt. Die Funktion `bfs()` gibt `true` zurück, wenn ein Pfad gefunden wurde, der vom Quellknoten zum Zielknoten führt und dessen Kapazität noch nicht vollständig ausgeschöpft ist. Andernfalls wird `false` zurückgegeben.

Als Datenstruktur der `bfs()` Funktion wird eine LinkedList verwendet. Die Laufzeit der `poll()` Funktion beträgt O(1), da die LinkedList eine doppelt verkettete Liste ist. Die Laufzeit der `add()` Funktion beträgt ebenfalls O(1), da die LinkedList eine doppelt verkettete Liste ist.

Wenn ein solcher Pfad gefunden wurde, wird der minimale Fluss des Pfades berechnet. Der minimale Fluss des Pfades ist die kleinste Kapazität, die noch nicht vollständig ausgeschöpft ist. Dieser Wert wird dann zum maximalen Fluss des Graphen addiert. 

Anschließend wird eine neue Ausgabematrix erstellt, die nur aus dem positiven Fluss des Graphen besteht. 

Zuletzt wird eine neue Ausgabematrix ohne Start- und Endknoten und deren Kanten erstellt. Diese Matrix wird dann als Ausgabe angezeigt und in eine Datei gespeichert.


```java
/**
 * Laufzeit: O(V * E^2 + V^2)
 * 
 * @param matrix
 * @return
 */
private int[][] fordFulkerson(int[][] matrix) {

  // Anzahl der Knoten im Graph
  int nodes = matrix[0].length;
  // Die Quelle is der erste Knoten
  int source = 0;
  // Die Senke ist der letzte Knoten
  int sink = nodes - 1;
  // Flow ist zu Beginn 0
  max_flow = 0;

  int u, v;

  // Erzeuge echte Kopie der Matrix für Output
  int output[][] = new int[nodes][nodes];
  for (u = 0; u < nodes; u++)
    for (v = 0; v < nodes; v++)
      output[u][v] = matrix[u][v];

  // Erzeuge ein Eltern Array zum speichern der möglichen BFS-Pfade
  int parent[] = new int[nodes];

  // Wenn für einen Pfad der BFS möglich ist, überprüfe seinen maximalen Fluss
  while (bfs(matrix, output, source, sink, parent)) {

    // Setze den Pfad Fluss auf unendlich
    int path_flow = Integer.MAX_VALUE;
    // Finde den maximalen Fluss durch die möglichen Pfade
    for (u = sink; u != source; u = parent[u]) {
      v = parent[u];
      path_flow = Math.min(path_flow, output[v][u]);
    }

    // aktualisiere die Kanten aus dem Eltern Array
    for (u = sink; u != source; u = parent[u]) {
      v = parent[u];
      // Ziehe den Fluss-Pfad den Kanten ab
      output[v][u] -= path_flow;
      // Addiere den Fluss-Pfad auf die Inversen Kanten
      output[u][v] += path_flow;
    }

    // Addiere die einzelnen Flusspfade auf den maximalen Fluss
    max_flow += path_flow;
  }

  // Ziehe von der Eingabe Matrix die übrigen Flussgewichte ab
  int[][] outputGraph = new int[nodes][nodes];
  for (int i = 0; i < matrix[0].length; i++)
    for (int j = 0; j < matrix[0].length; j++)
      outputGraph[i][j] = matrix[i][j] - output[i][j];

  return outputGraph;
}

/**
 * Laufzeit: O(V + E)
 * 
 * @param matrix
 * @param output
 * @param s
 * @param t
 * @param parent
 * @return
 */
private boolean bfs(int[][] matrix, int output[][], int s, int t, int parent[]) {

  // Anzahl der Knoten im Graph
  int nodes = matrix[0].length;

  // Array das alle Knoten als nicht besucht markiert
  boolean visited[] = new boolean[nodes];
  for (int i = 0; i < nodes; ++i)
    visited[i] = false;

  // Warteschlange, die besuchte Knoten als true markiert
  LinkedList<Integer> queue = new LinkedList<Integer>();
  queue.add(s);
  visited[s] = true;
  parent[s] = -1;

  // Standard BFS-Loop, entfernt Knoten aus der Warteschlange die ungleich 0 sind
  while (queue.size() != 0) {
    int u = queue.poll();

    for (int v = 0; v < nodes; v++) {
      if (visited[v] == false && output[u][v] > 0) {
        // Wenn wir einen möglichen Pfad von s nach t finden geben wir true zurück
        if (v == t) {
          parent[v] = u;
          return true;
        }
        // Wenn wir keinen möglichen Pfad finden fügen wir den Knoten zur Warteschlange
        // und markieren ihn als besichtigt
        queue.add(v);
        parent[v] = u;
        visited[v] = true;
      }
    }
  }
  return false;
}
```
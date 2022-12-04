# Problem 1 - "Straßen müssen her!"

Lange Zeit gab es in der Graphschaft Schilda einen Reformstau, kein Geld floss mehr in die Infrastruktur. Wie es kommen musste, wurde der Zustand der Stadt zusehends schlechter, bis die Bürger der Graphschaft den Aufbau Ihrer Stadt nun endlich selbst in die Hand nahmen. Zunächst einmal sollen neue Straßen gebaut werden. Zur Zeit gibt es nur einige schlammige Wege zwischen den Häusern. Diese sollen nun gepflastert werden, so dass von jedem Haus jedes andere Haus erreichbar ist. Da die Bürger der Stadt arm sind, soll der Straßenbau insgesamt möglichst wenig kosten. Die Bürger haben bereits einen Plan mit möglichen Wegen erstellt. Ihre Aufgabe ist nun, das kostengünstigste Wegenetz zu berechnen, so dass alle Häuser miteinander verbunden sind (nehmen Sie dabei pro Pflasterstein Kosten von 1 an):

## Modellierung des Problems

Das Problem lässt sich als Graphenmodell mit ungerichteten Kanten darstellen. Jedes Haus ist ein Knoten, die Straßen sind die Kanten. Die Kosten der Kanten sind die Kosten für die Pflastersteine. 

Es wird eine Konfiguration an Kanten gesucht, die eine minimale anzahl an Pflastersteinen benötigt.

Um den Graph zu modellieren werden die Java-Bibliotheken `JGraphT` und `JGraphX` verwendet. Mit `JGraphT` wird der Graph als Datenstruktur modelliert. Mit `JGraphX` wird der Graph als Grafik dargestellt und auf dem Bildschirm dargestellt.

## Die Eingabe

Die Eingabe besteht aus einem Graphen, der aus Kanten und Knoten besteht. Diese werden aus einer `.json` Datei gelesen und in eine Insanz der Klasse `GraphData.java` geladen. Diese Insanz dient als Basis für die Berechnung des günstigsten Weges.


``` json
{
  "directed_edges": false,
  "vertices": [
    { "label": "House 0" },
    { "label": "House 1" },
    ...
  ],
  "edges": [
    { "source": "House 0", "target": "House 1", "weight": 5 },
    { "source": "House 0", "target": "House 2", "weight": 3 },
    { "source": "House 0", "target": "House 4", "weight": 4 },
    ...
  ]
}
```

## Die Ausgabe

Die Ausgabe wird als Graph in einem Fenster dargestellt. Das Fenster besteht aus zwei Hälften. Auf der linken Seite wird der Eingabegraph dargestellt. Auf der rechten Seite wird der berechnete Graph dargestellt. 

Ein korrekte Ausgabe erfüllt folgende Eigenschaften:
- TODO

![Problem1](images/problem1.png)

## Geeignete Algorithmen

TODO Beschreibung MST mit Prim kruskal 

## Die Laufzeit des Algorithmus

TODO Laufzeitberechnung `O(|E| + |V| log |V|)`
TODO (Hier bitte auch eine Begründung einfügen, ein ausführlicher Beweis ist nicht notwendig.)

## Die Implementierung des Algorithmus

Zur Lösung des Problems wurde der Algorithmus von Prim implementiert. Als Datenstruktur wurde eine Prioritätswarteschlange verwendet, die Instanzen der Klasse `GraphVertex` beinhaltet: 

``` java
PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(
        Comparator.comparingInt(GraphVertex::getValue));
```

Für den Umgang mit Knoten und Kanten wurden drei Klassen implementiert:

- `GraphVertex.java`: Beinhaltet die Eigenschaft `int value`, welche den Key für den Algorithmus von Prim darstellt und das Objekt `GraphVertex predecessor`, der vom Algorithmus gesetzt wird.
- `GraphEdge.java`: Beinhaltet die Eigenschaften `String source`, `String target` und `double weight`.
- `GraphData.java`: Behinhaltet die Listen `ArrayList<GraphEdge>` und `ArrayList<GraphVertex>`

> Aufgrund der Struktur der `GraphVertex` und `GraphEdge` Klassen werden die zusätzlichen Funktionen `getNeighbors()` und `getEdgesBetweenTwoVertices()` benötigt. Diese Funktionen benötigen zusätzlicehe Laufzeit und werden in der Klasse `GraphData` implementiert.


``` java
// Initialisiere alle Knoten mit ∞, setze den Vorgänger auf null
for (GraphVertex v : vertices) {
  v.setValue(Integer.MAX_VALUE);
  v.setPredecessor(null);
}

// Starte mit beliebigem Startknoten, Startknoten bekommt den Wert 0
GraphVertex start = vertices.get(6);
start.setValue(0);

// Speichere alle Knoten in einer geeigneten Datenstruktur Q -> Prioritätswarteschlange
PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(Comparator.comparingInt(GraphVertex::getValue));
queue.addAll(vertices);

// Solange es noch Knoten in der Warteschlange gibt
while (!queue.isEmpty()) {
  // Wähle den Knoten aus Q mit dem kleinsten Schlüssel (v)
  GraphVertex vertex = queue.poll();
  // Für jeden Nachbarn von vertex 
  for (GraphVertex n : GraphData.getNeighbors(vertex, vertices, edges)) {
    // Für jede Kante zwischen vertex und n
    for (GraphEdge edge : GraphData.getEdgesBetweenTwoVertices(vertex, n, edges)) {
      // Wenn der Wert der Kante kleiner ist als der Wert des Knotens und der Knoten noch in Q ist
      if (edge.getWeight() < n.getValue() && queue.contains(n)) {
        // Speichere vertex als Vorgänger von n und passe den Wert von n an
        n.setValue((int) edge.getWeight());
        n.setPredecessor(vertex);
        // Aktualisiere die Prioritätswarteschlange
        queue.remove(n);
        queue.add(n);
      }
    }
  }
}
```

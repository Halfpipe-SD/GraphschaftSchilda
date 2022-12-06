# Problem 4 - "Historische Funde"

Beim Ausheben der Wege während des Straßenbaus wurde ein antiker Feuerwerksplan
gefunden. Die Lage der pyrotechnischen Effekte und die Zündschnüre sind noch sehr gut
zu erkennen.

Wie aber ist die Choreographie des Feuerwerks? In welcher Reihenfolge zünden die
Bomben? Können Sie den Bürgern der Graphschaft Schilda helfen? (Unter der Annahme,
dass die Zündschnur immer mit gleichbleibender Geschwindigkeit abbrennt...)

## Modellierung des Problems

Das Problem lässt sich als Graphenmodell mit ungerichteten Kanten darstellen. Das Steichholz und die Feuerwerkskörper sind die Knoten, die Zündschnüre sind die Kanten. Die Kosten der Kanten sind die Länge der Zündschnüre.

Es wird die korrekte Reihenfolge gesucht, in der die Feuerwerkskörper gezündet werden wenn das Streichholz den ersten Feuerwerkskörper zündet.

Um den Graph zu modellieren werden die Java-Bibliotheken `JGraphT` und `JGraphX` verwendet. Mit `JGraphT` wird der Graph als Datenstruktur modelliert. Mit `JGraphX` wird der Graph als Grafik dargestellt und auf dem Bildschirm dargestellt.

## Die Eingabe

Um das Bild der Aufgabenstellung in konkrete Daten zu übersetzen, wurden hier Schätzungen der Länge der Zünschnüre vorgenommen und in einer Grafik dargestellt.

![Problem4](images/problem4_input.png)

Die Eingabe besteht aus Knoten und Kanten, die aus einer `.json` Datei ausgelesen werden. 

``` json
{
  "directed_edges": false,
  "vertices": [
    { "label": "Match" },
    { "label": "Firecracker 1" },
    { "label": "Firecracker 2" },
    ...
  ],
  "edges": [
    { "source": "Match", "target": "Firecracker 1", "weight": 1 },
    { "source": "Firecracker 1", "target": "Firecracker 2", "weight": 4 },
    { "source": "Firecracker 1", "target": "Firecracker 3", "weight": 1 },
    ...
  ]
}
```

## Die Ausgabe

Die Ausgabe wird als Liste der Knoten dargestellt, in der die Knoten in der Reihenfolge aufgelistet sind, in der sie gezündet werden.

Ein korrekte Ausgabe erfüllt folgende Eigenschaften:

- TODO


![Problem4](images/problem4.png)

## Der Alrogithmus

TODO

## Die Laufzeit des Algorithmus

TODO

## Die Implementierung des Algorithmus

TODO


``` java
// Initialisiere die Distanz im Startknoten mit 0 und in allen anderen Knoten
// mit ∞.
for (GraphVertex vertex : vertices) {
  vertex.setValue(Integer.MAX_VALUE);
  vertex.setPredecessor(null);
}
vertices.get(0).setValue(0);

// Speichere alle Knoten in einer Prioritätswarteschlange queue
PriorityQueue<GraphVertex> queue = new PriorityQueue<GraphVertex>(
    Comparator.comparingInt(GraphVertex::getValue));
queue.addAll(vertices);

// Solange es noch unbesuchte Knoten gibt, wähle darunter denjenigen mit
// minimaler Distanz aus und
while (!queue.isEmpty()) {
  // Nehme den Knoten mit dem kleinsten Wert aus der Warteschlange
  GraphVertex v = queue.poll();

  // 1. speichere, dass dieser Knoten schon besucht wurde
  v.setVisited(true);

  // 2. berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen
  // Kantengewichtes und der Distanz im aktuellen Knoten
  for (GraphVertex n : GraphData.getNeighbors(v, vertices, edges)) {

    // 3. ist dieser Wert für einen Knoten kleiner als die
    // dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten
    // als Vorgänger. (Dieser Schritt wird auch als Update bezeichnet. )
    double sum = v.getValue() + GraphData.getWeightSum(v, n, edges);

    if (sum >= n.getValue())
      continue;

    n.setValue((int) sum);
    n.setPredecessor(v);
    // Aktualisiere die Prioritätswarteschlange
    queue.remove(n);
    queue.add(n);
  }
}

// Sortiere Knoten nach Distanz
vertices.sort(Comparator.comparingInt(GraphVertex::getValue));
```
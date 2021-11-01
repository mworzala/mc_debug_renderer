# Minecraft Debug Renderer

Allows servers to render arbitrarily placed and sized debug shapes on the client.

At the moment, the following plugin message is supported:

<table>
<thead>
  <tr>
    <th>PMC ID</th>
    <th colspan="5">Field Name</th>
    <th>Field Type</th>
    <th>Notes</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="15">debug:shapes</td>
    <td colspan="5">Op Count</td>
    <td>VarInt</td>
    <td>The number of operations following</td>
  </tr>
  <tr>
    <td rowspan="14">Operation</td>
    <td colspan="4">Type</td>
    <td>VarInt</td>
    <td>One of the following</td>
  </tr>
  <tr>
    <td rowspan="10">SET (0)</td>
    <td colspan="3">Target</td>
    <td>NamespacedID</td>
    <td></td>
  </tr>
  <tr>
    <td colspan="3">Shape Type</td>
    <td>VarInt</td>
    <td>One of the following</td>
  </tr>
  <tr>
    <td rowspan="3">BOX (0)</td>
    <td colspan="2">Start</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td colspan="2">End</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td colspan="2">Color</td>
    <td>Integer</td>
    <td>In ARGB format (8 bits per)</td>
  </tr>
  <tr>
    <td rowspan="5">LINES (1)</td>
    <td colspan="2">Point Count</td>
    <td>VarInt</td>
    <td>Number of following points. They will be drawn as a strip.</td>
  </tr>
  <tr>
    <td rowspan="2">Point</td>
    <td>Start</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td>End</td>
    <td>Vec3D</td>
    <td>3 doubles</td>
  </tr>
  <tr>
    <td colspan="2">Thickness</td>
    <td>Float</td>
    <td></td>
  </tr>
  <tr>
    <td colspan="2">Color</td>
    <td>Integer</td>
    <td>In ARGB format (8 bits per)</td>
  </tr>
  <tr>
    <td>REMOVE (1)</td>
    <td colspan="3">Target</td>
    <td>NamespaceID</td>
    <td></td>
  </tr>
  <tr>
    <td>CLEAR_NS (2)</td>
    <td colspan="3">Target</td>
    <td>String</td>
    <td>Clears all entries in the given namespace.</td>
  </tr>
  <tr>
    <td>CLEAR (3)</td>
    <td colspan="3"></td>
    <td></td>
    <td></td>
  </tr>
</tbody>
</table>

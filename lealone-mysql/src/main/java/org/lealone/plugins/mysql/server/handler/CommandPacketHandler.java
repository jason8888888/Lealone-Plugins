/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.plugins.mysql.server.handler;

import java.io.UnsupportedEncodingException;

import org.lealone.plugins.mysql.protocol.MySQLMessage;
import org.lealone.plugins.mysql.protocol.MySQLPacket;
import org.lealone.plugins.mysql.protocol.PacketInput;
import org.lealone.plugins.mysql.server.MySQLServerConnection;

public class CommandPacketHandler implements PacketHandler {

    // private static final Logger logger = LoggerFactory.getLogger(CommandPacketHandler.class);

    // private PacketHandler packetHandler;
    private final MySQLServerConnection conn;

    public CommandPacketHandler(MySQLServerConnection conn) {
        this.conn = conn;
    }

    public void setPacketHandler(PacketHandler packetHandler) {
        // this.packetHandler = packetHandler;
    }

    @Override
    public void handle(PacketInput in) {
        MySQLMessage mm = new MySQLMessage(in);
        switch (mm.type()) {
        case MySQLPacket.COM_QUERY:
            mm.position(5);
            String sql = null;
            try {
                // 使用指定的编码来读取数据
                sql = mm.readString("utf-8");
            } catch (UnsupportedEncodingException e) {
                return;
            }
            // logger.info("sql: " + sql);
            conn.executeStatement(sql);
            break;
        default:
        }
        // case MySQLPacket.COM_INIT_DB:
        // commands.doInitDB();
        // source.initDB(data);
        // break;
        // case MySQLPacket.COM_QUERY:
        // commands.doQuery();
        // source.query(data);
        // break;
        // case MySQLPacket.COM_PING:
        // commands.doPing();
        // source.ping();
        // break;
        // case MySQLPacket.COM_QUIT:
        // commands.doQuit();
        // source.close();
        // break;
        // case MySQLPacket.COM_PROCESS_KILL:
        // commands.doKill();
        // source.kill(data);
        // break;
        // case MySQLPacket.COM_STMT_PREPARE:
        // commands.doStmtPrepare();
        // source.stmtPrepare(data);
        // break;
        // case MySQLPacket.COM_STMT_EXECUTE:
        // commands.doStmtExecute();
        // source.stmtExecute(data);
        // break;
        // case MySQLPacket.COM_STMT_CLOSE:
        // commands.doStmtClose();
        // source.stmtClose(data);
        // break;
        // case MySQLPacket.COM_HEARTBEAT:
        // commands.doHeartbeat();
        // source.heartbeat(data);
        // break;
        // default:
        // commands.doOther();
        // source.writeErrMessage(ErrorCode.ER_UNKNOWN_COM_ERROR, "Unknown command");
        // }
    }
}

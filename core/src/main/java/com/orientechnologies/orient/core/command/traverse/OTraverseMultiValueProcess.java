/*
 * Copyright 2010-2012 Luca Garulli (l.garulli--at--orientechnologies.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.core.command.traverse;

import java.util.Iterator;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class OTraverseMultiValueProcess extends OTraverseAbstractProcess<Iterator<Object>> {
  private final OTraversePath parentPath;
  protected Object            value;
  protected int               index = -1;

  public OTraverseMultiValueProcess(final OTraverse iCommand, final Iterator<Object> iTarget, OTraversePath parentPath) {
    super(iCommand, iTarget);
    this.parentPath = parentPath;
  }

  public OIdentifiable process() {
    while (target.hasNext()) {
      value = target.next();
      index++;

      if (value instanceof OIdentifiable) {
        final ORecord<?> rec = ((OIdentifiable) value).getRecord();

        if (rec instanceof ODocument) {
          final OTraverseAbstractProcess<ODocument> subProcess = new OTraverseRecordProcess(command, (ODocument) rec, getPath());
          command.getContext().push(subProcess);

          return null;
        }
      }
    }

    return drop();
  }

  @Override
  public OTraversePath getPath() {
    return parentPath.appendIndex(index);
  }

  @Override
  public String toString() {
    return "[idx:" + index + "]";
  }
}
